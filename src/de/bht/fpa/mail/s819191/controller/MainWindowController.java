package de.bht.fpa.mail.s819191.controller;

import de.bht.fpa.mail.s819191.model.appLogic.FolderManager;
import de.bht.fpa.mail.s819191.model.appLogic.FolderManagerIF;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import de.bht.fpa.mail.s819191.model.data.Component;
import de.bht.fpa.mail.s819191.model.data.FileElement;
import de.bht.fpa.mail.s819191.model.data.Folder;
import java.io.File;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
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

    private final Image FOLDER_ICON = new Image("/de/bht/fpa/mail/s819191/icons/folder.png");
    private final Image FILE_ICON = new Image("/de/bht/fpa/mail/s819191/icons/file.png");
    private final File ROOT_PATH = new File(System.getProperty("user.home"));
    private List<File> historyList;
    private FolderManagerIF manager;

    @FXML
    private TreeView<Component> dirTree;
    @FXML
    private MenuItem menuItemOpen;
    @FXML
    private MenuItem menuItemHistory;

    public MainWindowController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree(ROOT_PATH);
        configureMenue();
    }

    private void configureTree(File root) {
        manager = new FolderManager(root);
        final TreeItem<Component> mainDirTree = new TreeItem<>(manager.getTopFolder(), new ImageView(FOLDER_ICON));
        mainDirTree.setExpanded(true);
        showItems(manager.getTopFolder(), mainDirTree);
        mainDirTree.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchExpand(e));
        dirTree.setRoot(mainDirTree);
    }

    public void branchExpand(final TreeModificationEvent<Component> e) {
        showItems((Folder) e.getTreeItem().getValue(), e.getTreeItem());
    }

    public void showItems(final Folder f, final TreeItem<Component> parent) {
        parent.getChildren().clear();
        f.getComponents().clear();
        manager.loadContent(f);
        final TreeItem<Component> DUMMY = new TreeItem<>(); //DUMMY ITEM
        f.getComponents().stream().forEach((Component com) -> {
            TreeItem<Component> z;
            if (com instanceof Folder) {
                z = new TreeItem<>(com, new ImageView(FOLDER_ICON));
                parent.getChildren().add(z);
                if (com.isExpandable()) {
                    z.getChildren().add(DUMMY);
                }
            } else if (com instanceof FileElement) {
                z = new TreeItem<>(com, new ImageView(FILE_ICON));
                parent.getChildren().add(z);
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
            
        }
    }

    private void directoryChoose() {
        Stage stage = new Stage();
        stage.setTitle("Open...");
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(ROOT_PATH);
        final File file = dc.showDialog(stage);
        configureTree(file);
    }
}
