package com.amplify.stofor.utils;

import android.app.Application;
import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class LimitedQueueRoboIntentServiceTest {

    private TestIntentService testIntentService;
    private Application application;

    @Before
    public void setUp() throws Exception {
        testIntentService = new TestIntentService();
        testIntentService.onCreate();
        application = Robolectric.application;
    }

    @Test
    public void shouldLimitTheSizeOfIntentQueue() throws Exception {
        assertThat(testIntentService.filterStartIntent(getIntentToStartTheService()), is(false));

        ShadowLooper.pauseLooper(testIntentService.mServiceLooper);
        testIntentService.onStartCommand(getIntentToStartTheService(), 0, 0);

        assertThat(testIntentService.filterStartIntent(mock(Intent.class)), is(true));

    }

    private class TestIntentService extends LimitedQueueRoboIntentService {

        public TestIntentService() {
            super(TestIntentService.class.getName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
        }
    }

    private Intent getIntentToStartTheService() {
        return new Intent(application, TestIntentService.class);
    }
}
