package com.zpi.plagiarism_detector.commons.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;

public class TestUtilsTest {
    @Test
    public void callPrivateMethodNoArgTestTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // given
        int startValue = 0;
        TestObj obj = new TestObj(startValue);

        // when
        TestUtils.callDeclaredMethod(obj, "incr1noArgVoid");

        // then
        Assert.assertEquals(obj.getValue(), startValue + 1);
    }

    @Test
    public void callPrivateMethodWithPrimitiveArgTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // given
        int startValue = 0;
        int additionValue = 2;
        TestObj obj = new TestObj(startValue);

        // when
        TestUtils.callDeclaredMethod(obj, "incrArgVoid", additionValue);

        // then
        Assert.assertEquals(obj.getValue(), startValue + additionValue);
    }

    @Test
    public void callPrivateMethodWithObjectArgTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // given
        String text1 = "1";
        String text2 = "2";
        TestObj obj = new TestObj(0);

        // when
        TestUtils.callDeclaredMethod(obj, "concatText", text1, text2);

        // then
        Assert.assertEquals(obj.getText(), " " + text1 + " " + text2);
    }

    private static class TestObj {

        int value;
        String text = "";

        public TestObj(int value) {
            this.value = value;
        }

        private void incr1noArgVoid() {
            this.value = value + 1;
        }

        private void incrArgVoid(int arg) {
            value = value + arg;
        }

        private void concatText(String text1, String text2) {
            text = text + " " + text1 + " " + text2;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

    }
}
