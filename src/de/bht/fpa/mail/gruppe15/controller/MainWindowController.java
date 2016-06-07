package de.bht.fpa.mail.gruppe15.controller;

import de.bht.fpa.mail.gruppe15.model.appLogic.EmailManager;
import de.bht.fpa.mail.gruppe15.model.appLogic.EmailManagerIF;
import de.bht.fpa.mail.gruppe15.model.appLogic.FolderManager;
import de.bht.fpa.mail.gruppe15.model.appLogic.FolderManagerIF;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import de.bht.fpa.mail.gruppe15.model.data.Component;
import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
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
    private static final File ROOT_PATH = new File(System.getProperty("user.dir"));
    /* ArrayList to save the history. */
    private final ArrayList<File> historyList = new ArrayList<>();
    /* Declaration of the needed managers for handling folders and emails */
    private FolderManagerIF folderManager;
    private final EmailManagerIF emailManager = new EmailManager();

    @FXML
    private TreeView<Component> dirTree;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem menuItemOpen;
    @FXML
    private MenuItem menuItemHistory;

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
        configureTree(ROOT_PATH);
        configureMenu();
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
     * @param root the file which shall be set to be shown in the TreeView.
     */
    public void configureTree(final File root) {
        folderManager = new FolderManager(root);
        final TreeItem<Component> rootItem;
        rootItem = new TreeItem<>(folderManager.getTopFolder(), new ImageView(FOLDER_ICON_OPEN));
        rootItem.setExpanded(true);
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchEvents(e));
        rootItem.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchEvents(e));
        showItems(folderManager.getTopFolder(), rootItem);
        dirTree.setRoot(rootItem);
    }

    /**
     * Method which handles the event when a treeitem gets expanded/collapsed.
     *
     * The expanded treeitem gets a new icon and the method showItems() is
     * called to show the new folders within this treeitem.
     *
     */
    private void branchEvents(final TreeModificationEvent<Component> e) {
        final TreeItem<Component> ti = e.getTreeItem();
        if (e.wasCollapsed()) {
            ti.setGraphic(new ImageView(FOLDER_ICON_CLOSED));
        }
        if (e.wasExpanded()) {
            ti.setGraphic(new ImageView(FOLDER_ICON_OPEN));
            showItems((Folder) ti.getValue(), ti);
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
        folderManager.loadContent(f);
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

    /**
     * Method which configures the menu.
     *
     * Every MenuItem gets the method menuEvents set on action.
     *
     */
    private void configureMenu() {
        menuBar.getMenus().stream().forEach((Menu m) -> {
            m.getItems().stream().forEach((mi) -> {
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
            configureTree(file);
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

        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
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
     * Method to print the emails found in the directory of the chosen treeitem
     * if it is not null.
     *
     */
    private void listMails(final TreeItem<Component> target) {
        if (target != null) {
            final Folder f;
            f = (Folder) target.getValue();
            emailManager.loadContent(f);
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
}
