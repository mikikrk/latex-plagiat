package com.zpi.plagiarism_detector.server.data;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class Monitor {
    private WeakHashMap map = new WeakHashMap() {
        public final Object get(Object key) {
            Object ref = super.get(key);
            Object monitor = ((ref == null) ? null : ((WeakReference) ref).get());
            if (monitor == null) {
                monitor = key;
                put(monitor, new WeakReference(monitor));
            }
            return monitor;
        }
    };

    public synchronized Object get(Object key) {
        return map.get(key);
    }
}

