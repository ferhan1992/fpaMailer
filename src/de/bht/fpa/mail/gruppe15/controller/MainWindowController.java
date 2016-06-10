package de.bht.fpa.mail.gruppe15.controller;

import de.bht.fpa.mail.gruppe15.model.appLogic.ApplicationLogic;
import de.bht.fpa.mail.gruppe15.model.appLogic.ApplicationLogicIF;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import de.bht.fpa.mail.gruppe15.model.data.Component;
import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class of the main window.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class MainWindowController implements Initializable {

    /* Initizializing the Icons for the TreeView */
    private static final Image FOLDER_ICON_CLOSED = new Image("/de/bht/fpa/mail/gruppe15/icons/folder.png");
    private static final Image FOLDER_ICON_OPEN = new Image("/de/bht/fpa/mail/gruppe15/icons/folder_open.png");
    /* Setting the standard root path to to dir of the main account. */
    private static final File ROOT_PATH = new File(System.getProperty("user.home"));
    /* ArrayList to save the history. */
    private final ArrayList<File> historyList = new ArrayList<>();
    /* OberservableList to save the loaded emails. */
    private final ObservableList<Email> emailList = FXCollections.observableArrayList();
    /* Declaration of the needed managers for handling folders and emails */
    private final ApplicationLogicIF appLogic = new ApplicationLogic(ROOT_PATH);

    @FXML
    private TreeView<Component> dirTree;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem menuItemOpen;
    @FXML
    private MenuItem menuItemSave;
    @FXML
    private MenuItem menuItemHistory;
    @FXML
    private TableView emailView;
    @FXML
    private TableColumn<Email, String> importanceColumn;
    @FXML
    private TableColumn<Email, String> receivedColumn;
    @FXML
    private TableColumn<Email, String> readColumn;
    @FXML
    private TableColumn<Email, String> senderColumn;
    @FXML
    private TableColumn<Email, String> recipientsColumn;
    @FXML
    private TableColumn<Email, String> subjectColumn;
    @FXML
    private TextField searchInput;
    @FXML
    private Label resultCount;
    @FXML
    private Label senderLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label receivedLabel;
    @FXML
    private Label receiverLabel;
    @FXML
    private TextArea outputArea;

    /**
     * Initializes the controller class. Starts configuring the TreeView with
     * using the method configureTree and the main root path. Starts configuring
     * the Menui with using the method configureMenu()
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        configureTree();
        configureMenu();
        configureTable();
        configureSearch();
        dirTree.getSelectionModel().selectedItemProperty().addListener((obs, old_val, new_val) -> listMails(new_val));
    }

    /**
     * Method to configure the main settings of the TreeView. The managers (for
     * email and folder) get initialized and the folderManager gets the file of
     * the dir which shall be build in the TreeView.
     *
     * Then, the root treeItem is set to the top folder of the given File. This
     * root folder is set to be expanded and gets two event handler. Those
     * handler manage what happens, when a TreeItem gets expanded or collapsed.
     *
     * The method to generate the TreeView is called with the current topfolder
     * of foldermanager and the root treeitem as variables.
     *
     * Finally the complete TreeView gets an eventhandler to manage what happens
     * when a treeitem gets chosen with a mouseclick. Calling the method to
     * print the emails found in the directory and the root of the dirTree gets
     * the root treeitem initialized before.
     *
     */
    public void configureTree() {
        final TreeItem<Component> rootItem;
        rootItem = new TreeItem<>(appLogic.getTopFolder(), new ImageView(FOLDER_ICON_OPEN));
        rootItem.setExpanded(true);
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchEvents(e));
        rootItem.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchEvents(e));
        showItems(appLogic.getTopFolder(), rootItem);
        dirTree.setRoot(rootItem);
        resetMailDetails();
    }

    /**
     * Method which handles the event when a treeitem gets expanded/collapsed.
     *
     * The expanded treeitem gets a new icon and the method showItems() is
     * called to show the new folders within this treeitem.
     *
     */
    private void branchEvents(final TreeModificationEvent<Component> e) {
        if (e != null) {
            final TreeItem<Component> ti = e.getTreeItem();
            if (e.wasCollapsed()) {
                ti.setGraphic(new ImageView(FOLDER_ICON_CLOSED));
            }
            if (e.wasExpanded()) {
                ti.setGraphic(new ImageView(FOLDER_ICON_OPEN));
                showItems((Folder) ti.getValue(), ti);
            }
        }
    }

    /**
     * Method to load the folder content and build the treeview. The treeitem
     * given as a parent gets their children cleared, so that every treeitem
     * wont be shown more than one time.
     *
     * The given Folder is used with the foldermanager to load the folder
     * content. And then we check for each component in f, whether it is a
     * Folder or not. If it is a folder, the new treeitem is generated and if
     * the component is expandable, a dummy treeItem is put into it, to let the
     * treeview show the expandable button next to the item.
     *
     * @param f The Folder which shall be scanned for new Folders/Files.
     * @param parent The Parent TreeItem.
     */
    private void showItems(final Folder f, final TreeItem<Component> parent) {
        if (f != null && parent != null) {
            appLogic.loadContent(f);
            parent.getChildren().clear();
            f.getComponents().stream().forEach((final Component com) -> {
                if (com instanceof Folder) {
                    final TreeItem<Component> item;
                    item = new TreeItem<>(com, new ImageView(FOLDER_ICON_CLOSED));
                    if (com.isExpandable()) {
                        final TreeItem<Component> DUMMY; //DUMMY ITEM
                        DUMMY = new TreeItem<>();
                        item.getChildren().add(DUMMY);
                    }
                    parent.getChildren().add(item);
                }
            });
        }
    }

    /**
     * Method which configures the menu.
     *
     * Every MenuItem gets the method menuEvents set on action.
     *
     */
    private void configureMenu() {
        menuBar.getMenus().stream().forEach((final Menu m) -> {
            m.getItems().stream().forEach((final MenuItem mi) -> {
                mi.setOnAction((e) -> menuEvents(e));
            });
        });
    }

    /**
     * Method which configures the eventhandler of the menu. The ActionEvent e
     * is checked, which menuitem was the source and the suitable method is
     * called.
     *
     * @param e The ActionEvent which was initialized.
     *
     */
    private void menuEvents(final ActionEvent e) {
        if (e.getSource() == menuItemOpen) {
            directoryChoose();
        }
        if (e.getSource() == menuItemHistory) {
            showHistory();
        }
        if (e.getSource() == menuItemSave) {
            mailSaver();
        }
    }

    /**
     * Method which opens a directorychooser, to choose the new main root for
     * the TreeView.
     *
     * The choosen file is checked, whether it is null and finally gets sent to
     * the method to configure the treeView and gets added to the historyList.
     *
     */
    private void directoryChoose() {
        final Stage stage;
        stage = new Stage();
        stage.setTitle("Open new directory...");
        final DirectoryChooser dc;
        dc = new DirectoryChooser();
        dc.setInitialDirectory(ROOT_PATH);
        final File file;
        file = dc.showDialog(stage);
        if (file != null) {
            /* && !(historyList.contains(file))) { */
            appLogic.changeDirectory(file);
            configureTree();
            historyList.add(file);
        }
    }

    /**
     * Method which loads the FXML for the history window and generates a new
     * stage for the history window.
     *
     */
    private void showHistory() {
        final Stage historyStage;
        historyStage = new Stage(StageStyle.UTILITY);
        historyStage.setTitle("Directory history...");
        final FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("/de/bht/fpa/mail/gruppe15/view/HistoryWindow.fxml"));
        loader.setController(new HistoryWindowController(this));
        try {
            Pane historyPane = (Pane) loader.load();
            final Scene scene;
            scene = new Scene(historyPane);
            historyStage.setScene(scene);
            historyStage.show();
        } catch (final Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    /**
     * Method to Save every Email in the current selected dir as XML Files in a
     * chosen directory.
     *
     */
    private void mailSaver() {
        final DirectoryChooser dc;
        dc = new DirectoryChooser();
        dc.setTitle("Save...");
        dc.setInitialDirectory(ROOT_PATH);
        final Stage saverStage;
        saverStage = new Stage(StageStyle.UTILITY);
        final File selectedDir;
        selectedDir = dc.showDialog(saverStage);
        if (selectedDir != null) {
            appLogic.saveEmails(emailList, selectedDir);
        }
    }

    /**
     * Method to get the historyList.
     *
     * @return Returns the history list as an ArrayList.
     */
    public ArrayList getHistory() {
        return historyList;
    }

    /**
     * Method for the main configuration of the TableView and the Columns.
     *
     * Each Column gets his cell value assignment in this method and the
     * standard sort configuration is set. The Comparator of the received column
     * gets set to order the Date in the right order.
     *
     */
    private void configureTable() {
        importanceColumn.setCellValueFactory(new PropertyValueFactory<>("importance"));
        receivedColumn.setCellValueFactory(new PropertyValueFactory<>("received"));
        readColumn.setCellValueFactory(new PropertyValueFactory<>("read"));
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        recipientsColumn.setCellValueFactory(new PropertyValueFactory<>("receiverTo"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

        receivedColumn.setComparator((receivedString1, receivedString2) -> compareReceived(receivedString1, receivedString2));
        emailView.getSortOrder().add(receivedColumn);

        emailView.getSelectionModel().selectedItemProperty().addListener((obs, old_val, new_val) -> showMailContent((Email) new_val));
    }

    /**
     * Method which fills the labels and the textarea with content taken from
     * the currently selected Email which is passed to it.
     *
     * @param selectedEmail The email which contents shall be shown.
     */
    private void showMailContent(final Email selectedEmail) {
        if (selectedEmail != null) {
            final Email email = selectedEmail;
            senderLabel.setText(email.getSender());
            subjectLabel.setText(email.getSubject());
            receivedLabel.setText(email.getReceived());
            receiverLabel.setText(email.getReceiver());
            outputArea.setText(email.getText());
        }
    }

    /**
     * Method to print and list the emails found in the directory of the chosen
     * treeitem if it is not null.
     *
     */
    private void listMails(final TreeItem<Component> target) {
        if (target != null) {
            final Folder f;
            f = (Folder) target.getValue();
            appLogic.loadEmails(f);
            resetMailDetails();
            emailList.addAll(f.getEmails());
            emailView.setItems(emailList);
            showItems(f, target);
            target.setValue(null);
            target.setValue(f);
            System.out.println("===========================================================================================================================================");
            System.out.println("Selected directory: " + f.getPath());
            System.out.println("Number of E-Mails: " + f.getEmails().size());
            if (f.getEmails().size() > 0) {
                f.getEmails().stream().forEach((final Email email) -> {
                    System.out.println(email.toString());
                });
            }
        }
    }

    /**
     * Method to resets the details shown from a selected xml email and to clear
     * the emailList. Needed if root directory gets changed.
     *
     */
    private void resetMailDetails() {
        emailList.clear();
        searchInput.setText("");
        resultCount.setText("");
        senderLabel.setText("");
        subjectLabel.setText("");
        receivedLabel.setText("");
        receiverLabel.setText("");
        outputArea.setText("");
    }

    /**
     * Method which configures the Search.
     *
     * An Listener gets added for the search field. Everytime something new is
     * written into it. The written value gets passed to the method to search
     * for the value.
     *
     */
    private void configureSearch() {
        searchInput.textProperty().addListener((obs, old_val, new_val) -> searchEvent(new_val));
    }

    /**
     * Method to configure what happens when something is searched.
     *
     * The input and the current emailList gets checked through and saved in an
     * observableList. Afterwards the emailView gets the items set to show the
     * items of the observablelist.
     *
     * The Result Counter gets modified depending on how much filtered mails are
     * present.
     *
     */
    private void searchEvent(final String input) {
        if (input != null) {
            final ObservableList filteredMails;
            filteredMails = (ObservableList) appLogic.search(emailList, input);
            emailView.setItems(filteredMails);
            resultCount.setText("(" + filteredMails.size() + ")");
            if (input.trim().equals("")) {
                resultCount.setText("");
            }
        }
    }

    /**
     * Method to configure how the comparison of the date Strings shall be
     * executed.
     *
     * @param receivedString1 variable holding a date as a String.
     * @param receivedString2 variable holding another date as a String.
     * @return int
     *
     */
    private int compareReceived(final String receivedString1, final String receivedString2) {
        Date receivedDate1 = null;
        Date receivedDate2 = null;
        if (receivedString1 != null && receivedString2 != null) {
            final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.GERMANY);
            try {
                receivedDate1 = format.parse(receivedString1);
                receivedDate2 = format.parse(receivedString2);
            } catch (final ParseException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return receivedDate1.compareTo(receivedDate2);
    }

    /**
     * Method to get the current instance of the application Logic.
     *
     * @return ApplicationLogicIF
     */
    public ApplicationLogicIF getAppLogic() {
        return this.appLogic;
    }
}
