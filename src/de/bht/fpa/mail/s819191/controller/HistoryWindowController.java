package de.bht.fpa.mail.s819191.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author FerhanKaplanseren
 */
public class HistoryWindowController implements Initializable {
    
    @FXML
    private ListView listViewHistory;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonOK;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonCancel.setOnAction((ActionEvent e) -> cancelButton());
        buttonOK.setOnAction((ActionEvent e) -> okButton());
        loadView();
    }
    
    private void loadView(){

    }

    private void cancelButton() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void okButton() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
