package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import sample.map.ControllerMap;

public class NewWindowController implements Initializable{
    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            initText();
    }

    public NewWindowController() throws IOException{
        ControllerMap.put(this);
    }

    public void initText()
    {
        usernameField.setText("");
        passwordField.setText("");
    }


}
