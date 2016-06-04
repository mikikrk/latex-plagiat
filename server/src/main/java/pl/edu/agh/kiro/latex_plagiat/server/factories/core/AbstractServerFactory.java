package pl.edu.agh.kiro.latex_plagiat.server.factories.core;

import pl.edu.agh.kiro.latex_plagiat.commons.factory.AbstractFactory;
import pl.edu.agh.kiro.latex_plagiat.server.core.Server;

import java.io.IOException;

public abstract class AbstractServerFactory extends AbstractFactory<Server> {
    public abstract Server create(int threadPoolSize, int portNumber) throws IOException;
}
