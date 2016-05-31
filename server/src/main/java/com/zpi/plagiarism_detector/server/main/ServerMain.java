package com.zpi.plagiarism_detector.server.main;

import com.zpi.plagiarism_detector.commons.protocol.ApplicationProperties;
import com.zpi.plagiarism_detector.server.ServerProperties;
import com.zpi.plagiarism_detector.server.core.Server;
import com.zpi.plagiarism_detector.server.factories.core.AbstractServerFactory;
import com.zpi.plagiarism_detector.server.factories.core.ServerFactory;

import java.io.IOException;

public class ServerMain {

    public static AbstractServerFactory serverFactory = new ServerFactory();

    public static void main(String[] args) {
        int threadPoolSize = determineThreadPoolSize(args);
        int portNumber = determinePortNumber(args);
        startUpServer(threadPoolSize, portNumber);
    }

    private static int determineThreadPoolSize(String[] args) {
        int threadPoolSize = ApplicationProperties.SERVER_THREAD_POOL_SIZE;
        if (args.length > 0) {
            try {
                threadPoolSize = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        return threadPoolSize;
    }

    private static int determinePortNumber(String[] args) {
        int portNumber = ApplicationProperties.PORT;
        if (args.length > 1) {
            try {
                portNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
            }
        }
        return portNumber;
    }

    private static void startUpServer(int threadPoolSize, int portNumber) {
        try {
            String docsPath = ServerProperties.DOCS_PATH;
            Server server = serverFactory.create(threadPoolSize, portNumber);
            server.handleConnections();
        } catch (IOException e1) {
            System.exit(-1);
        }
    }

}
