package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Folder;

/**
 * This is the interface for classes that manage emails.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public interface EmailManagerIF {

    /**
     * Loads all emails in the directory path of a folder into the given folder.
     *
     * @param f the folder into which the content of emails should be loaded
     */
    void loadContent(final Folder f);

    /**
     * Method to print the email content on console.
     *
     * @param f the folder which contains the emails.
     */
    void printContent(final Folder f);
}
