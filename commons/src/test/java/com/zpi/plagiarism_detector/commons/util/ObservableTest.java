package com.zpi.plagiarism_detector.commons.util;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Observer;

import static org.mockito.Mockito.*;

public class ObservableTest {
    private Observable observable;
    private Observer observer;

    @BeforeMethod
    public void init() {
        observer = mock(Observer.class);
        observable = new Observable();
    }

    @Test
    public void notifyObservers_NotifyObserverTriggersUpdateWithParameter() {
        // given
        Object notice = new Object();
        observable.addObserver(observer);

        // when
        observable.notifyObservers(notice);

        // then
        verify(observer, times(1)).update(observable, notice);
    }

    @Test
    public void notifyObservers_NotifyObserverTriggersUpdateWithNullParameter() {
        // given
        observable.addObserver(observer);

        // when
        observable.notifyObservers(null);

        // then
        verify(observer, times(1)).update(observable, null);
    }

    @Test
    public void notifyObservers_NotifyObserverTriggersUpdateWithoutParameter() {
        // given
        observable.addObserver(observer);

        // when
        observable.notifyObservers();

        // then
        verify(observer, times(1)).update(observable, null);
    }
}
