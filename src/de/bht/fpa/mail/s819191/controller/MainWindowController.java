/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s819191.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import de.bht.fpa.mail.s819191.model.data.Component;
import de.bht.fpa.mail.s819191.model.data.FileElement;
import de.bht.fpa.mail.s819191.model.data.Folder;
import java.io.File;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Ferhan
 */
public class MainWindowController implements Initializable {

    private final File ROOT_PATH = new File(System.getProperty("user.home"));
    private final Image FOLDER_ICON = new Image("/de/bht/fpa/mail/s819191/icons/folder.png");
    private final Image FILE_ICON = new Image("/de/bht/fpa/mail/s819191/icons/file.png");

    @FXML
    private TreeView<Component> dirTree;

    private void treeConfig() {
        TreeItem<Component> mainDir = new TreeItem<>(new Folder(ROOT_PATH, true), new ImageView(FOLDER_ICON));
        mainDir.setExpanded(false);
        mainDir.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent <Component> e) -> branchExpand(e));
        mainDir.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent <Component> e) -> branchCollapse(e));
        dirTree.setRoot(mainDir);
        treeContent(mainDir);
    }

    /**
     * Builds the TreeView of given dir taken from a TreeItem and checks whether
     * the loaded dir contains sub-directorys and/or files. If it contains dirs,
     * it checks whether the dir is empty or not to whether show or not show the
     * expandable Button next to the icon. Files will be showed without
     * expandable Button.
     *
     * @param x a TreeItem
     */
    public void treeContent(final TreeItem x) {
        File file = new File(((TreeItem<Component>) x).getValue().getPath());
        for (File y : file.listFiles()) {
            if (y.isDirectory()) {
                if (y.listFiles() != null) {
                    boolean isExpandable = true;
                    TreeItem<Folder> z = new TreeItem<>(new Folder(y.getAbsoluteFile(), isExpandable), new ImageView(FOLDER_ICON));
                    x.getChildren().add(z);
                } else if (y.listFiles() == null) {
                    boolean isExpandable = false;
                    TreeItem<Folder> z = new TreeItem<>(new Folder(y.getAbsoluteFile(), isExpandable), new ImageView(FOLDER_ICON));
                    x.getChildren().add(z);
                }
            } else if (y.isFile()) {
                TreeItem<FileElement> z = new TreeItem<>(new FileElement(y.getAbsoluteFile()), new ImageView(FILE_ICON));
                x.getChildren().add(z);
            }
        }
    }
    
    public void branchExpand(TreeModificationEvent <Component> e){
        treeContent(e.getTreeItem());
    }
    
    private void branchCollapse(TreeModificationEvent <Component> e) {
        TreeItem <Component> x = e.getSource();
        if (x != null){
            TreeItem <Component> y = x.getParent();
            if (y != null){
                y.getChildren().remove(x);
            }
        }
    }
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        treeConfig();
    }

}
