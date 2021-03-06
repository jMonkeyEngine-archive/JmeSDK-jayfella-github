package com.jayfella.sdk.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.ResourceBundle;

public class BuildOverlay implements Initializable {

    @FXML private Label statusLabel;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Button rebuildButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void bind(ObservableValue<String> observableValue) {
        statusLabel.textProperty().bind(observableValue);
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public Button getRebuildButton() {
        return rebuildButton;
    }


}
