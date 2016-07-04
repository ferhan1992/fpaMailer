package de.bht.fpa.mail.gruppe15.model.appLogic.xml;

import de.bht.fpa.mail.gruppe15.model.appLogic.EmailStrategyIF;
import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.io.FileFilter;
import javax.xml.bind.JAXB;

/**
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class XMLEmailStrategy implements EmailStrategyIF {

    /**
     * Loads all emails in the directory path of a folder into the given folder.
     * Checks if the email is in xml format.
     *
     * @param f the folder into which the content of emails should be loaded
     */
    @Override
    public void loadEmails(final Folder f) {
        if (f != null) {
            f.setContentLoaded();
            if (f.getEmails().isEmpty()) {
                final File file;
                file = new File(f.getPath());
                FileFilter filter;
                filter = (File filteredfile) -> filteredfile.getName().endsWith(".xml");
                for (final File fi : file.listFiles(filter)) {
                    final Email email = JAXB.unmarshal(fi, Email.class);
                    if (checkEmailFormat(email)) {
                        f.addEmail(email);
                    }
                }
            }
        }
    }

    /**
     * Method to check the passed XML file if it contains all the needed
     * variables to be accepted as a mail.
     *
     * @param email The XML File which gets checked.
     * @return boolean
     */
    private boolean checkEmailFormat(final Email email) {
        if (email != null) {
            return !(email.getSender() == null
                    || email.getImportance() == null
                    || email.getSubject() == null
                    || email.getText() == null
                    || email.getReceiver() == null);
        }
        return false;
    }
}
