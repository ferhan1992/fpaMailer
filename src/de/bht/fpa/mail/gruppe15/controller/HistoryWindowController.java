package de.bht.fpa.mail.gruppe15.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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

    private MainWindowController mainWindowController;

    HistoryWindowController(MainWindowController aThis) {
        mainWindowController = aThis;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonCancel.setOnAction((ActionEvent e) -> cancelButton());
        buttonOK.setOnAction((ActionEvent e) -> okButton());
        loadView();
    }

    private void loadView() {
        ArrayList<File> historyList = mainWindowController.getHistory();
        if (historyList.isEmpty()) {
            listViewHistory.getItems().add(new File("no base directories in history"));
            buttonOK.setDisable(true);
        } else if (!historyList.isEmpty()) {
            listViewHistory.getItems().addAll(historyList);
        }
    }

    private void cancelButton() {
        Stage historyStage = (Stage) buttonCancel.getScene().getWindow();
        historyStage.close();
    }

    private void okButton() {
        if (listViewHistory.getSelectionModel().getSelectedItem() != null) {
            mainWindowController.configureTree((File) listViewHistory.getSelectionModel().getSelectedItem());
            Stage historyStage = (Stage) buttonOK.getScene().getWindow();
            historyStage.close();
        } else {
            System.out.println("nothing choosed...");
        }
    }

}
