package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.util.List;
import javafx.collections.ObservableList;

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
     * Searches for all emails in the selected folder that contain the given
     * pattern.
     *
     * @param emailList List of loaded Emails
     * @param input Input in search field.
     * @return a list of all emails that contain the pattern
     */
    public ObservableList<Email> search(final ObservableList<Email> emailList, final String input);
}
