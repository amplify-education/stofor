package com.amplify.stofor.utils;

import android.content.Intent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(RobolectricTestRunner.class)
public class MessageQueueAwareIntentServiceTest {

    @Test
    public void shouldNotHaveIntentsScheduledAtCreation() throws Exception {
        TestIntentService service = new TestIntentService();
        service.onCreate();

        assertThat(service.hasIntentsScheduled(), is(false));
    }

    @Test
    public void shouldHaveIntentScheduledAfterReceivingAnIntent() throws Exception {
        TestIntentService service = new TestIntentService();
        service.onCreate();

        ShadowLooper.pauseLooper(service.mServiceLooper);

        service.onStartCommand(new Intent("fdsfd"), 0, 0);

        assertThat(service.hasIntentsScheduled(), is(true));
    }

    @Test
    public void shouldNotScheduleIntentIfFiltered() throws Exception {
        FilteringTestIntentService service = new FilteringTestIntentService();
        service.onCreate();

        ShadowLooper.pauseLooper(service.mServiceLooper);

        service.onStartCommand(new Intent("fdsfd"), 0, 0);

        assertThat(service.hasIntentsScheduled(), is(false));
    }

    public static class TestIntentService extends MessageQueueAwareIntentService {

        public TestIntentService() {
            super("Test Service");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
        }
    }

    public static class FilteringTestIntentService extends MessageQueueAwareIntentService {

        public FilteringTestIntentService() {
            super("Test Service");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
        }

        @Override
        protected boolean filterStartIntent(Intent intent) {
            return true;
        }
    }
}
