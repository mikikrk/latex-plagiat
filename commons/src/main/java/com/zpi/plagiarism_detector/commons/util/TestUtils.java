package com.zpi.plagiarism_detector.commons.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestUtils {
    public static <T> Object callDeclaredMethod(T obj, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class[] argclasses = extractArgClasses(args);
        Method method = obj.getClass().getDeclaredMethod(methodName, argclasses);
        method.setAccessible(true);
        return method.invoke(obj, args);
    }

    private static Class[] extractArgClasses(Object[] args) {
        Class[] classes = new Class[args.length];

        for (int i = 0; i < args.length; ++i) {
            Object currentObject = args[i];
            classes[i] = determineVarType(currentObject);
        }

        return classes;
    }

    private static Class<?> determineVarType(Object currentObject) {
        Class<?> aClass;
        if (TypeUtils.isPrimitiveType(currentObject)) {
            aClass = TypeUtils.obtainType(currentObject);
        } else {
            aClass = currentObject.getClass();
        }
        return aClass;
    }
}
