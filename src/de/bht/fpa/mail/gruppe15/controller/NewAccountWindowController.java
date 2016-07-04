/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.gruppe15.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class of the New Account Window
 *
 * @author FerhanKaplanseren
 */
public class NewAccountWindowController implements Initializable {

    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField hostInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonCancel.setOnAction((ActionEvent e) -> cancelButtonEvent());
        buttonSave.setOnAction((ActionEvent e) -> saveButtonEvent());

    }
    
    /**
     * Method to close the window without performing any actions.
     *
     * The current stage in which the cancel button is present is handed over to
     * a new variable of type stage. After that, the stage gets the close
     * command.
     */
    private void cancelButtonEvent() {
        close(buttonCancel);
    }

    private void saveButtonEvent() {
        if (nameInput.getText().trim().isEmpty() || hostInput.getText().trim().isEmpty() || usernameInput.getText().trim().isEmpty() || passwordInput.getText().trim().isEmpty()){
            errorLabel.setText("All textfields must contain data!");
        } else{
            errorLabel.setText("");
        }
    }
    
    /**
     * Method which interacts with the passed button, gets the Window of the
     * button and closes it.
     *
     * @param button the pressed Button which shall close the window.
     *
     */
    private void close(final Button button) {
        if (button != null) {
            final Stage newAccountStage;
            newAccountStage = (Stage) button.getScene().getWindow();
            newAccountStage.close();
        }
    }
}
