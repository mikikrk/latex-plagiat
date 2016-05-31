package com.zpi.plagiarism_detector.server.factories.core;

import com.zpi.plagiarism_detector.server.core.Server;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class ServerFactoryTest {

    private Server server;
    private AbstractServerFactory serverFactory;

    @BeforeTest
    public void init() {
        serverFactory = new ServerFactory();
    }

    @Test
    public void testCreateWithCorrectParams() throws IOException {
        // given
        int portNumber = 6666;
        int threadPoolSize = 4;

        // when
        server = serverFactory.create(threadPoolSize, portNumber);

        // then
        Assert.assertNotNull(server);
    }
}
