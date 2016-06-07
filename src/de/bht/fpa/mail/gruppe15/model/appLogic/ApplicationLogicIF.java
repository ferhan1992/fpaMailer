package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import javafx.collections.ObservableList;

/**
 * This Interface defines the methods which are needed to manage emails and
 * folders.
 *
 * @author Simone Strippgen
 */
public interface ApplicationLogicIF {

    /**
     * Get current root folder.
     *
     * @return current root folder.
     */
    Folder getTopFolder();

    /**
     * Loads all relevant content in the directory path of a folder into the
     * folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    void loadContent(final Folder f);

    /**
     * Searches for all emails in the selected folder that contain the given
     * pattern.
     *
     * @param emailList List of loaded Emails
     * @param input Input in search field.
     * @return a list of all emails that contain the pattern
     */
    public ObservableList<Email> search(final ObservableList<Email> emailList, final String input);

    /**
     * Loads all emails in the directory path of the given folder as objects of
     * the class Email into the folder.
     *
     * @param f the folder into which the emails of the corresponding
     * directory should be loaded
     */
    void loadEmails(final Folder f);

    /**
     * Changes the root directory of the application, and initializes the folder
     * manager with the new root directory.
     *
     * @param file the path to the directory which was selected as the new root
     * directory of the application.
     */
    void changeDirectory(final File file);

    /**
     * Saves the email objects of the selected folder into the given directory.
     *
     * @param file the path to the directory in which the email objects should
     * be saved.
     */
    void saveEmails(final File file);
}
