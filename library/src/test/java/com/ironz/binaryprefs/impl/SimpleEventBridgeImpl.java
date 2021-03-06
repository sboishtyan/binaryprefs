package com.ironz.binaryprefs.impl;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.ironz.binaryprefs.Preferences;
import com.ironz.binaryprefs.events.EventBridge;
import com.ironz.binaryprefs.events.OnSharedPreferenceChangeListenerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple preference change listener bridge. Uses current thread for delivering all events.
 */
public final class SimpleEventBridgeImpl implements EventBridge {

    private static final Map<String, List<OnSharedPreferenceChangeListener>> allListeners = new ConcurrentHashMap<>();

    private final List<OnSharedPreferenceChangeListener> listeners;

    public SimpleEventBridgeImpl(String prefName) {
        this.listeners = initListeners(prefName);
    }

    private List<OnSharedPreferenceChangeListener> initListeners(String prefName) {
        if (allListeners.containsKey(prefName)) {
            return allListeners.get(prefName);
        }
        List<OnSharedPreferenceChangeListener> listeners = new ArrayList<>();
        allListeners.put(prefName, listeners);
        return listeners;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListenerWrapper listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListenerWrapper listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListenersUpdate(Preferences preferences, String key, byte[] bytes) {
        notifyListeners(preferences, key);
    }

    @Override
    public void notifyListenersRemove(Preferences preferences, String key) {
        notifyListeners(preferences, key);
    }

    private void notifyListeners(Preferences preferences, String key) {
        for (OnSharedPreferenceChangeListener listener : listeners) {
            listener.onSharedPreferenceChanged(preferences, key);
        }
    }
}