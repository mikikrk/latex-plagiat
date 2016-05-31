package itest.com.zpi.plagiarism_detector.server.data;

import com.zpi.plagiarism_detector.server.data.FileData;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileData_CreateNewDirectoryAlreadyExistsExceptionIT {
    private FileData fileData;
    private Path path;
    private String pathString;

    @BeforeMethod
    public void init() throws IOException {
        fileData = new FileData();

        pathString = "./testPath";
        path = Paths.get(pathString);
        Files.createDirectory(path);

        boolean preExists = Files.exists(path);
        Assert.assertTrue(preExists);
    }

    @Test(expectedExceptions = FileAlreadyExistsException.class)
    public void createDirTest_createNewSuccess() throws IOException {
        // when
        fileData.createDir(pathString);
    }

    @AfterMethod
    public void cleanup() throws IOException {
        Files.delete(path);
    }
}

