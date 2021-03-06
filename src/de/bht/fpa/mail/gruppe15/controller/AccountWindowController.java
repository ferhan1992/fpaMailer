package de.bht.fpa.mail.gruppe15.controller;

import de.bht.fpa.mail.gruppe15.model.data.Account;
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
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 *
 */
public class AccountWindowController implements Initializable {

    @FXML
    private Button buttonExecute;
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

    private final MainWindowController mainWindowController;
    /* Account used by the Controller. */
    private final Account account;

    /**
     * FXML Controller class for the account window.
     *
     * @author Ferhan Kaplanseren
     * @author Ömür Düner
     */
    AccountWindowController(final Account account, final MainWindowController aThis) {
        mainWindowController = aThis;
        this.account = account;
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
        buttonExecute.setOnAction((ActionEvent e) -> execButtonEvent(e));
        if (account == null) {
            configureNewAccount();
        } else {
            configureEditAccount();
        }
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

    /**
     * Method managing the event occuring when pressing the button.
     *
     * @param e ActionEvent.
     *
     */
    private void execButtonEvent(final ActionEvent e) {
        if (nameInput.getText().trim().isEmpty() || hostInput.getText().trim().isEmpty() || usernameInput.getText().trim().isEmpty() || passwordInput.getText().trim().isEmpty()) {
            errorLabel.setText("All textfields must contain data!");
        } else {
            Account acc = new Account(nameInput.getText(), hostInput.getText(), usernameInput.getText(), passwordInput.getText());
            if (e.getSource().toString().endsWith("'Save'")) {
                mainWindowController.getAppLogic().saveAccount(acc);
            }
            if (e.getSource().toString().endsWith("'Update'")) {
                this.account.setPassword(passwordInput.getText());
                this.account.setUsername(usernameInput.getText());
                this.account.setHost(hostInput.getText());
                mainWindowController.getAppLogic().updateAccount(account);
            }
            mainWindowController.refreshAccountMenu();
            close(buttonExecute);
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

    /**
     * Method to configure the GUI for the new account event.
     */
    private void configureNewAccount() {
        buttonExecute.setText("Save");
    }

    /**
     * Method to configure the GUI for the edit account event.
     */
    private void configureEditAccount() {
        nameInput.setText(account.getName());
        hostInput.setText(account.getHost());
        usernameInput.setText(account.getUsername());
        passwordInput.setText(account.getPassword());
        buttonExecute.setText("Update");
        nameInput.setEditable(false);
    }
}
