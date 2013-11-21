package com.amplify.stofor;

import android.content.Intent;
import com.amplify.stofor.testinjectors.TestInjector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MetricSubmitIntentServiceTest {

    public static final String TEST_METRIC = "{ anyJson: true }";
    public static final String METRIC_SUBMISSION_SERVER = "http://www.amplify.com/metrics";

    private MetricSubmitIntentService service;
    @Mock
    private MetricUploadTaskQueue metricUploadTaskQueue;

    private Intent intent;
    private URL url;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        url = new URL(METRIC_SUBMISSION_SERVER);
        service = new MetricSubmitIntentService();
        new TestInjector<MetricSubmitIntentService>().
                bindInstance(MetricUploadTaskQueue.class, metricUploadTaskQueue).
                injectMocks(service);
        intent = new Intent(service, MetricSubmitIntentService.class);
    }

    @Test
    public void shouldPutUploadMetricTaskToTheQueue() {
        intent.putExtra(MetricSubmitIntentService.METRIC_BODY, TEST_METRIC);
        intent.putExtra(MetricSubmitIntentService.METRIC_SUBMISSION_URL, METRIC_SUBMISSION_SERVER);
        service.onHandleIntent(intent);

        verify(metricUploadTaskQueue).add(new MetricUploadTask(TEST_METRIC, url));
    }

    @Test
    public void shouldNotAddMetricForUploadIfNoMetricBodyProvided() {
        service.onHandleIntent(intent);

        verifyZeroInteractions(metricUploadTaskQueue);
    }

    @Test
    public void shouldNotAddMetricForUploadIfNoMetricBodyIsEmpty() {
        intent.putExtra(MetricSubmitIntentService.METRIC_BODY, "");
        service.onHandleIntent(intent);

        verifyZeroInteractions(metricUploadTaskQueue);
    }

    @Test
    public void shouldNotAddMetricIfInvalidURLForUploadWasProvided() {
        intent.putExtra(MetricSubmitIntentService.METRIC_BODY, TEST_METRIC);
        intent.putExtra(MetricSubmitIntentService.METRIC_SUBMISSION_URL, "fdsfsfdsfs");
        service.onHandleIntent(intent);

        verifyZeroInteractions(metricUploadTaskQueue);
    }

}
