package com.zpi.plagiarism_detector.client.view;

import javafx.scene.control.Alert;

public class ViewUtils {
    public static void showErrorDialog(String title, String header, String content) {
        createAlert(title, header, content, Alert.AlertType.ERROR);
    }

    public static void showWarningDialog(String title, String header, String content) {
        createAlert(title, header, content, Alert.AlertType.WARNING);
    }

    private static void createAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
