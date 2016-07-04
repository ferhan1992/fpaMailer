package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.util.List;

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
    void loadEmails(final Folder f);

    /**
     * Saves the email objects of the selected folder into the given directory.
     *
     * @param selectedDir the directory in which the email objects should be
     * saved.
     */
    void saveEmails(final File selectedDir);

    /**
     * Searches for all emails in the selected folder that contain the given
     * pattern.
     *
     * @param input Input in search field.
     * @return a list of all emails that contain the pattern
     */
    List<Email> search(final String input);
    
    void setEmailStrategy(final EmailStrategyIF strategy);

}
