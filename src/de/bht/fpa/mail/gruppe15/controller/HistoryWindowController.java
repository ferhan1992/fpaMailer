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

    private final MainWindowController mainWindowController;

    HistoryWindowController(final MainWindowController aThis) {
        mainWindowController = aThis;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
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
        final ArrayList<File> historyList;
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
     *
     * The current stage in which the cancel button is present is handed over to
     * a new variable of type stage. After that, the stage gets the close
     * command.
     */
    private void cancelButtonEvent() {
        close(buttonCancel);
    }

    /**
     * Method to choose the selected path from the history as the new root path
     * of active treeview of the main window. The ListView is checked for
     * whether it is empty or not. If its not empty, the choosen file of the
     * ListView is sent to the TreeView of the MainWindowController to show the
     * directory structure of it.
     *
     * The current stage in which the ok button is present is handed over to a
     * new variable of type stage. After that, the stage gets the close command.
     *
     * If nothing is choosen but ok is clicked, an error message is printed out.
     */
    private void okButtonEvent() {
        if (listViewHistory.getSelectionModel().getSelectedItem() != null) {
            final File fi = (File) listViewHistory.getSelectionModel().getSelectedItem();
            mainWindowController.getAppLogic().changeDirectory(fi);
            mainWindowController.configureTree(fi);
            close(buttonOK);
        } else {
            System.out.println("Error: No directory of history choosen...");
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
            final Stage historyStage;
            historyStage = (Stage) button.getScene().getWindow();
            historyStage.close();
        }
    }
}
