package com.zpi.plagiarism_detector.server;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ServerPropertiesTest {
    @Test
    public void testRefleciton() {
        Field[] declaredFields = ServerProperties.class.getDeclaredFields();
        List<String> staticFieldNames = new ArrayList<>();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                staticFieldNames.add(field.getName());
            }
        }

        Assert.assertTrue(staticFieldNames.contains("DOCS_PATH"));
    }
}
