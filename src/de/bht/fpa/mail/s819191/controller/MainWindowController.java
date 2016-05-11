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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final TreeItem<Component> DUMMY = new TreeItem<>();

    @FXML
    private TreeView<Component> dirTree;

    private void configureTree() {
        final Folder MAIN_FOLDER = new Folder(ROOT_PATH, false);
        final TreeItem<Component> mainDirTree = new TreeItem<>(MAIN_FOLDER, new ImageView(FOLDER_ICON));
        showItems(MAIN_FOLDER, mainDirTree);
        mainDirTree.setExpanded(true);
        dirTree.setRoot(mainDirTree);
        mainDirTree.addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchExpand(e));
        mainDirTree.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeItem.TreeModificationEvent<Component> e) -> branchCollapse(e));
    }

    public void branchExpand(final TreeModificationEvent<Component> e) {
        showItems((Folder) e.getSource().getValue(), e.getTreeItem());
    }

    private void branchCollapse(final TreeModificationEvent<Component> e) {
        e.getTreeItem().getChildren().removeAll(e.getTreeItem().getChildren());
        e.getTreeItem().getChildren().add(DUMMY);
    }

    public ArrayList<Component> loadContent(final Folder f) {
        final ArrayList<Component> al = new ArrayList<>();
        final File file = new File(f.getPath());
        for (File fi : file.listFiles()) {
            if (fi.isDirectory()) {
                if (fi.list().length == 0) {
                    Folder y = new Folder(fi.getAbsoluteFile(), false);
                    al.add(y);
                } else {
                    Folder y = new Folder(fi.getAbsoluteFile(), true);
                    al.add(y);
                }
            } else {
                FileElement y = new FileElement(fi.getAbsoluteFile());
                al.add(y);
            }
        }
        return al;
    }

    public void showItems(final Folder f, final TreeItem parent) {
        loadContent(f).stream().forEach((com) -> {
            File file = new File(com.getPath());
            if (com.getClass().getName().contains("Folder")) {
                if (com.isExpandable()) {
                    TreeItem<Component> z = new TreeItem<>(new Folder(file, true), new ImageView(FOLDER_ICON));
                    parent.getChildren().add(z);
                    z.getChildren().add(DUMMY);
                } else if (!com.isExpandable()) {
                    TreeItem<Component> z = new TreeItem<>(new Folder(file, false), new ImageView(FOLDER_ICON));
                    parent.getChildren().add(z);
                }
            } else {
                TreeItem<Component> z = new TreeItem<>(new FileElement(file), new ImageView(FILE_ICON));
                parent.getChildren().add(z);
            }
        });
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
    }

}
