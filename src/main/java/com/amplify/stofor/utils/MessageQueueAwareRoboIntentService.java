package com.amplify.stofor.utils;

import android.content.Intent;
import android.content.res.Configuration;
import com.google.inject.Injector;
import com.google.inject.Key;
import roboguice.RoboGuice;
import roboguice.event.EventManager;
import roboguice.service.event.OnConfigurationChangedEvent;
import roboguice.service.event.OnCreateEvent;
import roboguice.service.event.OnDestroyEvent;
import roboguice.service.event.OnStartEvent;
import roboguice.util.RoboContext;

import java.util.HashMap;
import java.util.Map;

public abstract class MessageQueueAwareRoboIntentService  extends MessageQueueAwareIntentService implements RoboContext {

    protected EventManager eventManager;
    protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();



    public MessageQueueAwareRoboIntentService(String name) {
        super(name);
    }


    @Override
    public void onCreate() {
        final Injector injector = RoboGuice.getInjector(this);
        eventManager = injector.getInstance(EventManager.class);
        injector.injectMembers(this);
        super.onCreate();
        eventManager.fire(new OnCreateEvent() );
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        eventManager.fire(new OnStartEvent() );
    }


    @Override
    public void onDestroy() {
        try {
            if(eventManager!=null) // may be null during test: http://code.google.com/p/roboguice/issues/detail?id=140
                eventManager.fire(new OnDestroyEvent() );
        } finally {
            try {
                RoboGuice.destroyInjector(this);
            } finally {
                super.onDestroy();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        final Configuration currentConfig = getResources().getConfiguration();
        super.onConfigurationChanged(newConfig);
        eventManager.fire(new OnConfigurationChangedEvent(currentConfig,newConfig) );
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }
}
