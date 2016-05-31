package com.zpi.plagiarism_detector.server.factories.core;

import com.zpi.plagiarism_detector.commons.factory.AbstractFactory;
import com.zpi.plagiarism_detector.server.core.Server;

import java.io.IOException;

public abstract class AbstractServerFactory extends AbstractFactory<Server> {
    public abstract Server create(int threadPoolSize, int portNumber) throws IOException;
}
