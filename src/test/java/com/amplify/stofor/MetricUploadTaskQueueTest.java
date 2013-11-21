package com.amplify.stofor;

import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class MetricUploadTaskQueueTest {

    private static final String METRIC_TO_UPLOAD = "{}";
    private MetricUploadTaskQueue queue;
    private URL url;

    @Before
    public void setUp() throws Exception {
        url = new URL("http://www.amplify.com/metrics");
        queue = new MetricUploadTaskQueue(new MetricUploadDelegateQueue(mock(GsonConverter.class), Robolectric.application), Robolectric.application);
    }

    @Test
    public void shouldStartUploadingMetricsOnPostingANewOne() throws Exception {
        queue.add(new MetricUploadTask(METRIC_TO_UPLOAD, url));

        Intent nextStartedService = Robolectric.getShadowApplication().getNextStartedService();
        assertThat(nextStartedService.getComponent().getClassName(), is(MetricUploadIntentService.class.getName()));
    }

}
