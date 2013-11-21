package com.amplify.stofor;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(RobolectricTestRunner.class)
public class GsonConverterTest {

    public static final String METRIC_UPLOAD_SERVER = "http://www.amplify.com/metrics";
    private GsonConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new GsonConverter(new Gson());
    }

    @Test
    public void shouldUseGsonToInflateAndDeflate() throws Exception {
        MetricUploadTask task = new MetricUploadTask("{}", new URL(METRIC_UPLOAD_SERVER));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        converter.toStream(task, outputStream);
        MetricUploadTask deserializedTask = converter.from(outputStream.toByteArray());
        assertThat(deserializedTask, is(task));
    }
}
