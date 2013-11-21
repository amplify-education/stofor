package com.amplify.stofor.testinjectors;

import com.amplify.httpclient.AmplifyHttpClientModule;
import com.google.inject.AbstractModule;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import org.robolectric.Robolectric;
import roboguice.RoboGuice;

import java.util.HashMap;
import java.util.Map;

public class TestInjector<T> {

    private final Map<Class, Object> classesToBindToMocks = new HashMap<Class, Object>();

    public <Type> TestInjector<T> bindInstance(Class<Type> classToMock, Type instanceOfClassToMock) {
        classesToBindToMocks.put(classToMock, instanceOfClassToMock);
        return this;
    }

    public void injectMocks(Object testCase) {
        RoboGuice.setBaseApplicationInjector(Robolectric.application, Stage.DEVELOPMENT,
                Modules.override(new AmplifyHttpClientModule()).with(new UnitTestOverrideModule()),
                RoboGuice.newDefaultRoboModule(Robolectric.application));

        RoboGuice.getInjector(Robolectric.application).injectMembers(testCase);
    }

    private class UnitTestOverrideModule extends AbstractModule {
        @Override
        protected void configure() {
            for (Map.Entry<Class, Object> entry : classesToBindToMocks.entrySet()) {
                bind(entry.getKey()).toInstance(entry.getValue());
            }
        }
    }
}
