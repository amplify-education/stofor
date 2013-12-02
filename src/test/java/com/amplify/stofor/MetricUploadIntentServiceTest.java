package com.amplify.stofor;

import com.amplify.httpclient.AmplifyHttpClient;
import com.amplify.httpclient.AmplifyHttpClientFactory;
import com.amplify.stofor.testinjectors.TestInjector;
import com.amplify.stofor.utils.LimitedQueueRoboIntentService;
import com.amplify.stofor.utils.NetworkConnectivity;
import com.amplify.stofor.utils.ThreadUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static com.amplify.stofor.MetricUploadIntentService.MAX_TIME_BETWEEN_RETIRES;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MetricUploadIntentServiceTest {

    private MetricUploadIntentService service;
    @Mock
    private MetricUploadTaskQueue metricUploadTaskQueue;
    @Mock
    private MetricUploadTask task;
    @Mock
    private AmplifyHttpClientFactory amplifyHttpClientFactory;
    @Mock
    private AmplifyHttpClient httpClient;
    @Mock
    private ThreadUtils threadUtils;
    @Mock
    private NetworkConnectivity networkConnectivity;
    @Mock
    private MetricUploadTask task2;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(amplifyHttpClientFactory.getOKHttpBasedClient(5, 5, true)).thenReturn(httpClient);
        when(networkConnectivity.isConnected()).thenReturn(true);
        service = new MetricUploadIntentService();

        new TestInjector<MetricUploadIntentService>().
                bindInstance(MetricUploadTaskQueue.class, metricUploadTaskQueue).
                bindInstance(AmplifyHttpClientFactory.class, amplifyHttpClientFactory).
                bindInstance(NetworkConnectivity.class, networkConnectivity).
                bindInstance(ThreadUtils.class, threadUtils).
                injectMocks(service);
    }

    @Test
    public void shouldBeOfClassLimitedQueueRoboIntentService() {
        assertTrue(service instanceof LimitedQueueRoboIntentService);
    }

    @Test
    public void shouldPeekAndExecuteTasksUntilQueueIsEmpty() throws Exception {
        when(metricUploadTaskQueue.peek()).thenReturn(task).thenReturn(task2).thenReturn(null);

        service.onHandleIntent(null);

        verify(task).executeUsingNetwork(httpClient, service);
        verify(task2).executeUsingNetwork(httpClient, service);
    }

    @Test
    public void shouldDeleteTopTaskPeekNextOneOnExecutionSuccess() throws Exception {
        service.onSuccess();

        verify(metricUploadTaskQueue).remove();
    }

    @Test
    public void shouldWaitSomeTimeOnNetworkFailureBeforeRetry() {
        service.onNetworkFailure(null);

        verify(threadUtils).sleepRandom(MAX_TIME_BETWEEN_RETIRES);
    }

    @Test
    public void shouldStopOnNoNetwork() {
        when(metricUploadTaskQueue.peek()).thenReturn(task);
        when(networkConnectivity.isNotConnected()).thenReturn(true);

        service.onHandleIntent(null);

        verify(task, never()).executeUsingNetwork(httpClient, service);
    }

    @Test
    public void shouldRemoveInvalidMetricsFromTheQueue() {
        service.onInvalidMetric(new AmplifyHttpClient.Response(400, ""));

        verify(metricUploadTaskQueue).remove();
    }


    @Test
    public void shouldDeleteItemFromTheQueueOnParseException() {
        when(metricUploadTaskQueue.peek()).thenThrow(new RuntimeException()).thenReturn(task).thenReturn(null);

        service.onHandleIntent(null);

        verify(metricUploadTaskQueue).remove();
        verify(task).executeUsingNetwork(httpClient, service);
    }
}
