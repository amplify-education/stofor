package com.amplify.stofor;

import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.squareup.tape.FileObjectQueue;

import java.io.File;
import java.io.IOException;

@Singleton
public class MetricUploadDelegateQueue extends FileObjectQueue<MetricUploadTask> {

    private static final String QUEUE_FILENAME = "metrics_queue_file";

    @Inject
    public MetricUploadDelegateQueue(GsonConverter converter, Context context) throws IOException {
        super(new File(context.getFilesDir(), QUEUE_FILENAME), converter);
    }

}
