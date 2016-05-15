package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import javax.xml.bind.DataBindingException;
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
     *
     * @param f the folder into which the content of emails should be loaded
     */
    @Override
    public void loadContent(final Folder f) {
        final File file = new File(f.getPath());
        if (f.getEmails().isEmpty()) {
            for (final File fi : file.listFiles()) {
                if (fi.isFile() && fi.getName().endsWith(".xml")) {
                    try {
                        f.addEmail(JAXB.unmarshal(fi, Email.class));
                    } catch (DataBindingException e) {
                        System.out.println("XML is not conform to be used. See following details: " + e.getMessage());
                    }
                }
            }
        }
    }
}
