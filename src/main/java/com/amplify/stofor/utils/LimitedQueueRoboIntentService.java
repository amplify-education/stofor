package com.amplify.stofor.utils;

import android.content.Intent;

public abstract class LimitedQueueRoboIntentService extends MessageQueueAwareRoboIntentService {

    public LimitedQueueRoboIntentService(String name) {
        super(name);
    }

    @Override
    protected boolean filterStartIntent(Intent intent) {
        return hasIntentsScheduled();
    }


}
