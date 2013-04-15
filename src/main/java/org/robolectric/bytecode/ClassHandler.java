package org.robolectric.bytecode;

public interface ClassHandler {
    void classInitializing(Class clazz);

    Object initializing(Object instance);

    Plan methodInvoked(String signature, boolean isStatic, Class<?> theClass);

    Object intercept(String signature, Object instance, Object[] paramTypes, Class theClass) throws Throwable;

    <T extends Throwable> T stripStackTrace(T throwable);

    public interface Plan {
        Object run(Object instance, Object[] params) throws Throwable;
    }
}
