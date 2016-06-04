package pl.edu.agh.kiro.latex_plagiat.commons.util;

import java.util.Hashtable;
import java.util.Map;

public class TypeUtils {
    private final static Map<Class<?>, Class<?>> primitives = new Hashtable<Class<?>, Class<?>>();

    static {
        primitives.put(Boolean.class, boolean.class);
        primitives.put(Byte.class, byte.class);
        primitives.put(Short.class, short.class);
        primitives.put(Integer.class, int.class);
        primitives.put(Long.class, long.class);
        primitives.put(Float.class, float.class);
        primitives.put(Double.class, double.class);
        primitives.put(Character.class, char.class);
    }


    public static boolean isPrimitiveType(final Object object) {
        if (object == null) {
            return false;
        }
        return isPrimitiveType(object.getClass());
    }

    public static boolean isPrimitiveType(final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return primitives.containsKey(clazz);
    }

    public static Class<?> obtainType(final Object object) {
        if (isPrimitiveType(object)) {
            return primitives.get(object.getClass());
        } else {
            return object.getClass();
        }
    }
}
