package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import javax.xml.bind.JAXB;

/**
 * This class manages emails and their content which is loaded from a given
 * Folder.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class EmailManager implements EmailManagerIF {

    /**
     * Loads all emails in the directory path of a folder into the given folder.
     * Checks if the email is in xml format.
     *
     * @param f the folder into which the content of emails should be loaded
     */
    @Override
    public void loadContent(final Folder f) {
        if (f.getEmails().isEmpty()) {
            final File file;
            file = new File(f.getPath());
            FileFilter filter;
            filter = (File filteredfile) -> filteredfile.getName().endsWith(".xml");
            for (final File fi : file.listFiles(filter)) {
                f.addEmail(JAXB.unmarshal(fi, Email.class));
            }
        }
    }

    /**
     * Method to print the email content on console.
     *
     * @param f the folder which contains the emails.
     */
    @Override
    public void printContent(final Folder f) {
        System.out.println("Selected directory: " + f.getPath());
        System.out.println("Number of E-Mails: " + f.getEmails().size());
        if (f.getEmails().size() > 0) {
            f.getEmails().stream().forEach((final Email email) -> {
                System.out.println(email.toString());
            });
        }
    }
}
