package com.amplify.stofor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkConnectivityTest {

    @Mock
    private Context context;
    @Mock
    private NetworkInfo networkInfo;
    private NetworkConnectivity networkConnectivity;
    @Mock
    private ConnectivityManager connectivityManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        networkConnectivity = new NetworkConnectivity(context);
    }

    @Test
    public void shouldBeTrueIfNetworkInfoIsConnected() {
        when(networkInfo.isConnected()).thenReturn(true);
        assertThat(networkConnectivity.isConnected(), is(true));
    }

    @Test
    public void shouldBeFalseIfNetworkIsNotConnected() {
        when(networkInfo.isConnected()).thenReturn(false);
        assertThat(networkConnectivity.isConnected(), is(false));
    }

    @Test
    public void shouldBeFalseIfNetworkInfoIsNull() {
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);
        assertThat(networkConnectivity.isConnected(), is(false));
    }
}
