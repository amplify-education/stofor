package com.amplify.stofor;

import com.amplify.stofor.utils.LogUtils;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.squareup.tape.FileObjectQueue;
import org.slf4j.Logger;

import java.io.*;


public class GsonConverter implements FileObjectQueue.Converter<MetricUploadTask> {

    private static final Logger LOG = LogUtils.getLogger();
    private final Gson gson;

    @Inject
    public GsonConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public MetricUploadTask from(byte[] bytes) {
        String string = new String(bytes);
        LOG.info("Picked up metric JSON from the queue: " + string);
        Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes));
        return gson.fromJson(reader, MetricUploadTask.class);
    }

    @Override
    public void toStream(MetricUploadTask object, OutputStream bytes) throws IOException {
        Writer writer = new OutputStreamWriter(bytes);
        gson.toJson(object, writer);
        writer.close();
    }
}