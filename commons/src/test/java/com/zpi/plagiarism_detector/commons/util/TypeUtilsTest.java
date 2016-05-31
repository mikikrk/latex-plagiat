package com.zpi.plagiarism_detector.commons.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TypeUtilsTest {
    private Class<?>[] types;
    private Object[] objects;

    @Test
    public void isPrimitiveTypeClassCorrectValuesTest() {
        // given
        types = new Class[]{
                Boolean.class,
                Byte.class,
                Short.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                Character.class,
                String.class
        };

        // when
        boolean[] result = new boolean[types.length];
        for (int i = 0; i < types.length; ++i) {
            result[i] = TypeUtils.isPrimitiveType(types[i]);
        }

        // then
        for (int i = 0; i < result.length - 1; ++i) {
            Assert.assertTrue(result[i]);
        }

        Assert.assertFalse(result[result.length - 1]);
    }

    @Test
    public void isPrimitiveTypeObjNullArgTest() {
        // given
        Object obj = null;

        // when
        boolean result = TypeUtils.isPrimitiveType(obj);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void isPrimitiveTypeClassNullArgTest() {
        // given
        Class classType = null;

        // when
        boolean result = TypeUtils.isPrimitiveType(classType);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void isPrimitiveTypeObjectCorrectValuesTest() {
        // given
        objects = new Object[]{
                new Object(),
                new String("aaa"),
                5,
                7,
                "bbb"
        };

        // when
        boolean[] result = new boolean[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            result[i] = TypeUtils.isPrimitiveType(objects[i]);
        }

        // then
        Assert.assertFalse(result[0]);
        Assert.assertFalse(result[1]);
        Assert.assertTrue(result[2]);
        Assert.assertTrue(result[3]);
        Assert.assertFalse(result[4]);
    }

    @Test
    public void obtainTypePrimitiveTest() {
        // given
        Object obj = 7;

        // when
        Class<?> aClass = TypeUtils.obtainType(obj);

        // then
        Assert.assertEquals(aClass, int.class);
    }

    @Test
    public void obtainTypeObjectTest() {
        // given
        Object obj = new String();

        // when
        Class<?> aClass = TypeUtils.obtainType(obj);

        // then
        Assert.assertEquals(aClass, String.class);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void obtainTypeNullArgTest() {
        // given
        Class<?> classType = null;

        // when
        Class<?> aClass = TypeUtils.obtainType(classType);
    }
}
