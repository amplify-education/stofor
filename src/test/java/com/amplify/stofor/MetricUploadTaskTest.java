package com.amplify.stofor;

import com.amplify.httpclient.AmplifyHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.URL;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MetricUploadTaskTest {

    private static final String METRIC_REPRESENTATION = "{}";

    @Mock
    private AmplifyHttpClient httpClient;
    @Mock
    private MetricUploadTask.Callback callback;

    private MetricUploadTask metricUploadTask;
    private URL url;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        url = new URL("http://www.amplify.com/metrics");
        metricUploadTask = new MetricUploadTask(METRIC_REPRESENTATION, url);
    }

    @Test
    public void shouldCallOnSuccessIfMetricUploadSucceeded() throws Exception {
        when(httpClient.post(url, METRIC_REPRESENTATION, null)).thenReturn(new AmplifyHttpClient.Response(200, ""));
        metricUploadTask.executeUsingNetwork(httpClient, callback);

        verify(callback).onSuccess();
    }

    @Test
    public void shouldCallOnFailureOnIOExceptionWhileUploadingTheMetric() throws IOException {
        IOException ioException = new IOException();
        when(httpClient.post(url, METRIC_REPRESENTATION, null)).thenThrow(ioException);
        metricUploadTask.executeUsingNetwork(httpClient, callback);

        verify(callback).onNetworkFailure(ioException);
    }

    @Test
    public void shouldCallOnInvalidMetricIfServerReturnsAnErrorCode() throws IOException {
        AmplifyHttpClient.Response response = new AmplifyHttpClient.Response(400, "invalid metric");
        when(httpClient.post(url, METRIC_REPRESENTATION, null)).thenReturn(response);
        metricUploadTask.executeUsingNetwork(httpClient, callback);

        verify(callback).onInvalidMetric(response);
    }
}
