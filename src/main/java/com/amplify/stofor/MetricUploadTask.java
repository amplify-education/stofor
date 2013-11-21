package com.amplify.stofor;

import com.amplify.httpclient.AmplifyHttpClient;
import com.amplify.stofor.utils.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;


public class MetricUploadTask extends NetworkTask<MetricUploadTask.Callback> {

    private static final Logger LOG = LogUtils.getLogger();

    private final String metricRepresentation;
    private final URL metricSubmissionURL;

    public MetricUploadTask(String metricRepresentation, URL metricSubmissionURL) {
        this.metricRepresentation = metricRepresentation;
        this.metricSubmissionURL = metricSubmissionURL;
    }

    @Override
    public void executeUsingNetwork(AmplifyHttpClient httpClient, Callback callback) {
        LOG.debug("Starting metric upload: {} to {}", metricRepresentation, metricSubmissionURL);
        try {
            AmplifyHttpClient.Response response = httpClient.post(metricSubmissionURL, metricRepresentation, null);
            if (response.isSuccess()) {
                callback.onSuccess();
                LOG.debug("Successfully uploaded metric: " + metricRepresentation);
            } else {
                // TODO: be more smart parsing server busy responses
                callback.onInvalidMetric(response);
            }
        } catch (IOException e) {
            callback.onNetworkFailure(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MetricUploadTask that = (MetricUploadTask) o;

        if (metricRepresentation == null ? that.metricRepresentation != null : !metricRepresentation.equals(that.metricRepresentation)) {
            return false;
        }
        if (metricSubmissionURL == null ? that.metricSubmissionURL != null : !metricSubmissionURL.equals(that.metricSubmissionURL)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = metricRepresentation == null ? 0 : metricRepresentation.hashCode();
        result = 31 * result + (metricSubmissionURL == null ? 0 : metricSubmissionURL.hashCode());
        return result;
    }

    public interface Callback {
        void onSuccess();

        void onNetworkFailure(IOException e);

        void onInvalidMetric(AmplifyHttpClient.Response response);
    }
}
