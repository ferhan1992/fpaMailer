/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import javafx.collections.ObservableList;

/**
 * This class manages the methods which are needed to manage emails and folders.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class ApplicationLogic implements ApplicationLogicIF {

    /* Declaration of the needed managers for handling folders and emails */
    private EmailManagerIF emailManager;
    private FolderManagerIF folderManager;

    /**
     * Constructor of the Class, initializing new instances of FolderManager and
     * EmailManager.
     *
     * @param directory
     */
    public ApplicationLogic(final File directory) {
        folderManager = new FolderManager(directory);
        emailManager = new EmailManager();
    }

    /**
     * Get current root folder.
     *
     * @return current root folder.
     */
    @Override
    public Folder getTopFolder() {
        final Folder f;
        f = folderManager.getTopFolder();
        return f;
    }

    /**
     * Loads all relevant content in the directory path of a folder into the
     * folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    @Override
    public void loadContent(final Folder f) {
        folderManager.loadContent(f);
    }

    /**
     * Searches for all emails in the selected folder that contain the given
     * pattern.
     *
     * @param emailList List of loaded Emails
     * @param input Input in search field.
     * @return a list of all emails that contain the pattern
     */
    @Override
    public ObservableList<Email> search(ObservableList<Email> emailList, String input) {
        final ObservableList<Email> filteredList;
        filteredList = emailManager.search(emailList, input);
        return filteredList;
    }

    /**
     * Loads all emails in the directory path of the given folder as objects of
     * the class Email into the folder.
     *
     * @param f the folder into which the emails of the corresponding directory
     * should be loaded
     */
    @Override
    public void loadEmails(final Folder f) {
        emailManager.loadEmails(f);
    }

    /**
     * Changes the root directory of the application, and initializes the folder
     * manager with the new root directory.
     *
     * @param file the path to the directory which was selected as the new root
     * directory of the application.
     */
    @Override
    public void changeDirectory(final File file) {
        folderManager = new FolderManager(file);
        emailManager = new EmailManager();
    }

    /**
     * Saves the email objects of the selected folder into the given directory.
     *
     * @param emailList The list of Emails in current Folder.
     * @param selectedDir the directory in which the email objects should be
     * saved.
     */
    @Override
    public void saveEmails(final ObservableList<Email> emailList, final File selectedDir) {
        emailManager.saveEmails(emailList, selectedDir);
    }
}
