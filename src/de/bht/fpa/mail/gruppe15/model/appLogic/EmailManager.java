package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
                final Email email = JAXB.unmarshal(fi, Email.class);
                if (checkEmailFormat(email)) {
                    f.addEmail(email);
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
        return !(email.getSubject() == null
                || email.getText() == null
                || email.getReceiver() == null
                || email.getSender() == null
                || email.getImportance() == null);
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
    public ObservableList<Email> search(final ObservableList<Email> emailList, final String input) {
        final ObservableList filteredMails = FXCollections.observableArrayList();
        emailList.stream().filter((final Email email) -> email.getSubject().toLowerCase().contains(input.trim().toLowerCase())
                || email.getText().toLowerCase().contains(input.trim().toLowerCase())
                || email.getReceived().toLowerCase().contains(input.trim().toLowerCase())
                || email.getSent().toLowerCase().contains(input.trim().toLowerCase())
                || email.getReceiver().toLowerCase().contains(input.trim().toLowerCase())
                || email.getSender().toLowerCase().contains(input.trim().toLowerCase())).forEach((email) -> {
            filteredMails.add(email);
        });
        return filteredMails;
    }
}
