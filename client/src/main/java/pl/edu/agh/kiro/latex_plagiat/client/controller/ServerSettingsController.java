/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.kiro.latex_plagiat.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Agat
 */
public class ServerSettingsController implements Initializable, Controller {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button codeCancelButton, applyServButton;
    
    @FXML
    TextField hostnameField, portField;

    @FXML
    private void handleCodeCancelButtonAction(ActionEvent event) {

        System.out.println("You clicked me, dumbass!");
        Stage stage = (Stage) codeCancelButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleApplyServButtonAction(ActionEvent event) {

        System.out.println("You clicked me, dumbass!");
        Stage stage = (Stage) codeCancelButton.getScene().getWindow();
        pl.edu.agh.kiro.latex_plagiat.client.controller.MainSceneController.setServerAndPort(hostnameField.getText(), Integer.parseInt(portField.getText()));
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        hostnameField.setText(pl.edu.agh.kiro.latex_plagiat.client.controller.MainSceneController.getHostname());
        portField.setText(Integer.toString(pl.edu.agh.kiro.latex_plagiat.client.controller.MainSceneController.getPort()));
    }

}
