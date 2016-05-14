package de.bht.fpa.mail.gruppe15.controller;

import de.bht.fpa.mail.gruppe15.model.appLogic.EmailManager;
import de.bht.fpa.mail.gruppe15.model.appLogic.FolderManager;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ferhan
 */
public class MainWindowController implements Initializable {

    private final Image FOLDER_ICON_CLOSED = new Image("/de/bht/fpa/mail/gruppe15/icons/folder.png");
    private final Image FOLDER_ICON_OPEN = new Image("/de/bht/fpa/mail/gruppe15/icons/folder_open.png");
    private final File ROOT_PATH = new File((System.getProperty("user.dir") + "/src/de/bht/fpa/mail/gruppe15/Account"));
    private final ArrayList<File> historyList = new ArrayList<>();
    private FolderManager folderManager;
    private EmailManager emailManager;
    
    @FXML
    private TreeView<Component> dirTree;
    @FXML
    private MenuItem menuItemOpen;
    @FXML
    private MenuItem menuItemHistory;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree(ROOT_PATH);
        configureMenue();
    }

    public void configureTree(File root) {
        folderManager = new FolderManager(root);
        emailManager = new EmailManager();
        final TreeItem<Component> mainDirTree = new TreeItem<>(folderManager.getTopFolder(), new ImageView(FOLDER_ICON_OPEN));
        mainDirTree.setExpanded(true);
        showItems(folderManager.getTopFolder(), mainDirTree);
        mainDirTree.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchExpand(e));
        mainDirTree.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchCollapse(e));
        dirTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> emailPrint(newValue));
        dirTree.setRoot(mainDirTree);
    }

    private void branchExpand(final TreeModificationEvent<Component> e) {
        final TreeItem<Component> ti = e.getTreeItem();
        ti.setGraphic(new ImageView(FOLDER_ICON_OPEN));
        showItems((Folder) ti.getValue(), ti);
    }

    private void branchCollapse(final TreeModificationEvent<Component> e) {
        final TreeItem<Component> ti = e.getTreeItem();
        ti.setGraphic(new ImageView(FOLDER_ICON_CLOSED));
    }

    private void showItems(final Folder f, final TreeItem<Component> parent) {
        parent.getChildren().clear();
        folderManager.loadContent(f);
        final TreeItem<Component> DUMMY = new TreeItem<>(); //DUMMY ITEM
        f.getComponents().stream().forEach((Component com) -> {
            TreeItem<Component> z;
            if (com instanceof Folder) {
                z = new TreeItem<>(com, new ImageView(FOLDER_ICON_CLOSED));
                parent.getChildren().add(z);
                if (com.isExpandable()) {
                    z.getChildren().add(DUMMY);
                }
            }
        });
    }

    private void configureMenue() {
        menuItemOpen.setOnAction((e) -> menuEvents(e));
        menuItemHistory.setOnAction((e) -> menuEvents(e));
    }

    private void menuEvents(ActionEvent e) {
        if (e.getSource() == menuItemOpen) {
            directoryChoose();
        }
        if (e.getSource() == menuItemHistory) {
            showHistory();
        }
    }

    private void directoryChoose() {
        Stage stage = new Stage();
        stage.setTitle("Open...");
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(ROOT_PATH);
        final File file = dc.showDialog(stage);
        if (file != null) {
            configureTree(file);
            historyList.add(file);
        }
    }

    private void showHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/bht/fpa/mail/gruppe15/view/HistoryWindow.fxml"));
            loader.setController(new HistoryWindowController(this));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage historyStage = new Stage();
            historyStage.setTitle("Select Base Directory");
            historyStage.setScene(scene);
            historyStage.show();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public ArrayList getHistory() {
        return historyList;
    }

    private void emailPrint(final TreeItem<Component> parent) {
        final Folder f = (Folder) parent.getValue();
        emailManager.loadContent(f);
        System.out.println("Selected directory: " + f.getPath());
        System.out.println("Number of E-Mails: " + f.getEmails().size());
        for (Email email :f.getEmails()){
            System.out.println(email.toString());
        }
    }
}
