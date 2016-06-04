package pl.edu.agh.kiro.latex_plagiat.client.model.factories;

import pl.edu.agh.kiro.latex_plagiat.client.model.core.Client;
import pl.edu.agh.kiro.latex_plagiat.commons.factory.AbstractFactory;

import java.io.IOException;

public abstract class AbstractClientFactory extends AbstractFactory<pl.edu.agh.kiro.latex_plagiat.client.model.core.Client> {
    public abstract Client create(String hostName, int port) throws IOException;
}
