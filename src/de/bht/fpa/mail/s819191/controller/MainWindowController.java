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
import de.bht.fpa.mail.s819191.model.data.Folder;
import java.io.File;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Ferhan
 */
public class MainWindowController implements Initializable {

    private static final String ROOT_PATH = System.getProperty("user.dir");
    private final Image FOLDER_ICON = new Image("/de/bht/fpa/mail/s819191/icons/folder.png");
    private final Image FILE_ICON = new Image("/de/bht/fpa/mail/s819191/icons/file.png");

    @FXML
    private TreeView<Component> dirTree;

    private void configureTree() {
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
