package com.amplify.stofor.utils;

import com.google.inject.Inject;

public class ThreadUtils {
    @Inject
    private RandomNumberProvider randomNumberProvider;

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //Ignore
        }
    }

    public void sleepRandom(long maxSleepTimeMillis) {
        sleep((long) (randomNumberProvider.getDouble() * maxSleepTimeMillis));
    }
}
