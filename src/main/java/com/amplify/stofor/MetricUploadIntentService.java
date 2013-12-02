package com.amplify.stofor;

import android.content.Intent;
import com.amplify.httpclient.AmplifyHttpClient;
import com.amplify.httpclient.AmplifyHttpClientFactory;
import com.amplify.stofor.utils.LimitedQueueRoboIntentService;
import com.amplify.stofor.utils.LogUtils;
import com.amplify.stofor.utils.NetworkConnectivity;
import com.amplify.stofor.utils.ThreadUtils;
import com.google.inject.Inject;
import org.slf4j.Logger;

import java.io.IOException;

public class MetricUploadIntentService extends LimitedQueueRoboIntentService implements MetricUploadTask.Callback {

    private static final Logger LOG = LogUtils.getLogger();
    public static final long MAX_TIME_BETWEEN_RETIRES = 5000l;

    @Inject
    private MetricUploadTaskQueue queue;
    @Inject
    private AmplifyHttpClientFactory httpClientFactory;
    @Inject
    private ThreadUtils threadUtils;
    @Inject
    private NetworkConnectivity networkConnectivity;

    private AmplifyHttpClient httpClient;

    public MetricUploadIntentService() {
        super(MetricUploadIntentService.class.getName());
        // will recreate the service after it was killed
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            MetricUploadTask task;

            try {
                task = queue.peek();
            } catch (RuntimeException ex) {
                LOG.error("Runtime exception while picking from the queue: " + ex);
                queue.remove();
                continue;
            }

            if (task == null || networkConnectivity.isNotConnected()) {
                return;
            }
            task.executeUsingNetwork(getHttpClient(), this);
        }
    }

    @Override
    public void onSuccess() {
        queue.remove();
    }

    @Override
    public void onNetworkFailure(IOException e) {
        LOG.error("Network error while uploading a metric: {}. Gonna retry the upload.", e);
        threadUtils.sleepRandom(MAX_TIME_BETWEEN_RETIRES);
    }

    @Override
    public void onInvalidMetric(AmplifyHttpClient.Response response) {
        LOG.error("Skipping metric that was rejected by the server, error code: {}, body: {}",
                response.getStatusCode(), response.getBody());
        queue.remove();
    }

    private AmplifyHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = httpClientFactory.getOKHttpBasedClient(5, 5, true);
        }
        return httpClient;
    }
}
