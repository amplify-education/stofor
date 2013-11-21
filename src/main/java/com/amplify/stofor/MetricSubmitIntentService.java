package com.amplify.stofor;

import android.content.Intent;
import com.amplify.stofor.utils.LogUtils;
import com.google.inject.Inject;
import org.slf4j.Logger;
import roboguice.service.RoboIntentService;

import java.net.MalformedURLException;
import java.net.URL;

public class MetricSubmitIntentService extends RoboIntentService {

    private static final Logger LOG = LogUtils.getLogger();
    public static final String METRIC_BODY = "metric_body";
    public static final String METRIC_SUBMISSION_URL = "metric_submission_url";

    @Inject
    private MetricUploadTaskQueue metricUploadTaskQueue;

    public MetricSubmitIntentService() {
        super(MetricSubmitIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String metricBody = intent.getStringExtra(METRIC_BODY);

        if (metricBody == null || metricBody.equals("")) {
            return;
        }

        URL metricSubmissionURL;
        try {
            metricSubmissionURL = new URL(intent.getStringExtra(METRIC_SUBMISSION_URL));
        } catch (MalformedURLException e) {
            LOG.error("Exception while trying to submit a metric", e);
            return;
        }


        metricUploadTaskQueue.add(new MetricUploadTask(metricBody, metricSubmissionURL));
    }
}
