/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.kiro.latex_plagiat.client.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * @author Agat
 */
public class SwitchButton extends Label {

    @FXML
    private Button switchButton;

    private SimpleBooleanProperty articleSwitchedOn = new SimpleBooleanProperty(true);

    public SwitchButton() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/includes/SwitchButton.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            this.setPrefWidth(90);
            switchButton.setPrefWidth(40);
            setStyle("-fx-background-color: gray;");
            switchButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    articleSwitchedOn.set(!articleSwitchedOn.get());
                }
            });

            setGraphic(switchButton);

            articleSwitchedOn.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov,
                                    Boolean t, Boolean t1) {
                    if (!t1) {
                        setText("Article");
                        setStyle("-fx-text-fill:white;");
                        setContentDisplay(ContentDisplay.RIGHT);
                    } else {
                        setText("Code");
                        setStyle("-fx-text-fill:white;");
                        setContentDisplay(ContentDisplay.LEFT);
                    }
                }
            });

            articleSwitchedOn.set(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SimpleBooleanProperty switchOnProperty() {
        return articleSwitchedOn;
    }
}
