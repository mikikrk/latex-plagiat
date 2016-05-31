package com.zpi.plagiarism_detector.server.data;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MonitorTest {
    private Monitor monitor;

    @BeforeMethod
    public void init() {
        monitor = new Monitor();
    }

    @Test
    public void isNewFileIsNotCreatingFileTest() {
        // given
        String pathString = "./file";
        Path path = Paths.get(pathString);

        // when
        File file = new File(pathString);

        // then
        Assert.assertFalse(Files.exists(path));
    }

    @Test
    public void testFileEquality() {
        // given
        String pathString = "./file";
        Path path = Paths.get(pathString);

        File file1 = new File(pathString);
        File file2 = new File(pathString);

        // when
        boolean areFileObjEqual = file1.equals(file2);

        // then
        Assert.assertTrue(areFileObjEqual);
    }
}