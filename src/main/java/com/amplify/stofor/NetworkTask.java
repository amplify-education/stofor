package com.amplify.stofor;

import com.amplify.httpclient.AmplifyHttpClient;
import com.squareup.tape.Task;

public abstract class NetworkTask<T> implements Task<T> {

    public abstract void executeUsingNetwork(AmplifyHttpClient amplifyHttpClient, T callback);

    /**
     * Override executeUsingNetwork instead.
     */
    @Deprecated
    @Override
    public void execute(T callback) {
        return;
    }
}
