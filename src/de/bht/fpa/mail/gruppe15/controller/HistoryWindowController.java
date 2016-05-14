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
 * FXML Controller class for the history window.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonCancel.setOnAction((ActionEvent e) -> cancelButtonEvent());
        buttonOK.setOnAction((ActionEvent e) -> okButtonEvent());
        loadView();
    }

    /**
     * Method to load the history content.
     *
     * Method takes the historyList from the main WindowController and checks,
     * whether it is empty or not.
     *
     * If empty, the ok button will be disabled and the text "no base
     * directories in history" will be shown.
     *
     * If not empty the listView gets the Items of historyList added.
     */
    private void loadView() {
        ArrayList<File> historyList;
        historyList = mainWindowController.getHistory();
        if (historyList.isEmpty()) {
            listViewHistory.getItems().add(new File("no base directories in history"));
            buttonOK.setDisable(true);
        } else if (!historyList.isEmpty()) {
            listViewHistory.getItems().addAll(historyList);
        }
    }

    /**
     * Method to close the history window without performing any actions.
     */
    private void cancelButtonEvent() {
        Stage historyStage = (Stage) buttonCancel.getScene().getWindow();
        historyStage.close();
    }

    /**
     * Method to choose the selected path from the history as the new root path
     * of active treeview of the main window.
     *
     * if nothing is choosen, an error message is printed out.
     */
    private void okButtonEvent() {
        if (listViewHistory.getSelectionModel().getSelectedItem() != null) {
            mainWindowController.configureTree((File) listViewHistory.getSelectionModel().getSelectedItem());
            Stage historyStage = (Stage) buttonOK.getScene().getWindow();
            historyStage.close();
        } else {
            System.out.println("Error: No directory of history choosen...");
        }
    }

}
