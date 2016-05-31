package com.zpi.plagiarism_detector.client.model.factories;

import com.zpi.plagiarism_detector.client.model.core.Client;
import com.zpi.plagiarism_detector.commons.factory.AbstractFactory;

import java.io.IOException;

public abstract class AbstractClientFactory extends AbstractFactory<Client> {
    public abstract Client create(String hostName, int port) throws IOException;
}
