package com.amplify.stofor;

import android.content.Context;
import android.content.Intent;
import com.google.inject.Inject;
import com.squareup.tape.TaskQueue;

public class MetricUploadTaskQueue extends TaskQueue<MetricUploadTask> {

    private final Context context;

    @Inject
    public MetricUploadTaskQueue(MetricUploadDelegateQueue metricUploadDelegateQueue, Context context) {
        super(metricUploadDelegateQueue);
        this.context = context;
    }

    @Override
    public void add(MetricUploadTask entry) {
        super.add(entry);
        context.startService(new Intent(context, MetricUploadIntentService.class));
    }
}
