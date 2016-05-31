package com.zpi.plagiarism_detector.commons.util;

public class Observable extends java.util.Observable {
    @Override

    /**
     * Powiadamia obserwatorów o zmianie stanu obiektu
     * @param arg argument przekazany do poinformowania obserwatorów
     */
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }

    @Override
    /**
     * Powiadamia obserwatorów o zmianie stanu obiektu
     */
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
