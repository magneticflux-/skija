package org.jetbrains.skija;

import java.lang.ref.Cleaner;

public abstract class Managed extends Native implements AutoCloseable {
    public final boolean _allowClose;
    public Cleaner.Cleanable _cleanable;

    public Managed(long ptr, long finalizerPtr) {
        this(ptr, finalizerPtr, true);
    }

    public Managed(long ptr, long finalizerPtr, boolean allowClose) {
        super(ptr);
        String className = getClass().getSimpleName();
        Stats.onAllocated(className);
        this._cleanable = _cleaner.register(this, new CleanerThunk(className, ptr, finalizerPtr));
        this._allowClose = allowClose;
    }

    @Override
    public void close() {
        if (_allowClose) {
            _cleanable.clean();
            _cleanable = null;
            _ptr = 0;
        } else
            throw new RuntimeException("close() is not allowed on " + this);
    }

    public static Cleaner _cleaner = Cleaner.create();

    public static class CleanerThunk implements Runnable {
        public String _className;
        public long _ptr;
        public long _finalizer;

        public CleanerThunk(String className, long ptr, long finalizer) {
            this._className = className;
            this._ptr = ptr;
            this._finalizer = finalizer;
        }

        public void run() {
            Stats.onDeallocated(_className);
            Stats.onNativeCall();
            _nInvokeFinalizer(_finalizer, _ptr);
        }
    }

    public static native void _nInvokeFinalizer(long finalizerPtr, long ptr);
}