package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Folder;

/**
 * This is the interface for the email strategy.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public interface EmailStrategyIF {

    /**
     * Loads all emails in the directory path of a folder into the given folder.
     *
     * @param f the folder into which the content of emails should be loaded
     */
    void loadEmails(final Folder f);
}
