package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.controller.MainWindowController;
import de.bht.fpa.mail.gruppe15.model.appLogic.xml.EmailStrategy;
import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * This class manages emails and their content which is loaded from a given
 * Folder.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class EmailManager implements EmailManagerIF {

    private Folder selectedFolder;

    /**
     * Loads all emails in the directory path of a folder into the given folder.
     * Checks if the email is in xml format.
     *
     * @param f the folder into which the content of emails should be loaded
     */
    @Override
    public void loadEmails(final Folder f) {
        if (f != null) {
            selectedFolder = f;
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
     * Saves the email objects of the selected folder into the given directory.
     *
     * @param selectedDir the directory in which the email objects should be
     * saved.
     */
    @Override
    public void saveEmails(final File selectedDir) {
        if (selectedDir != null) {
            try {
                JAXBContext jaxbContext;
                jaxbContext = JAXBContext.newInstance(Email.class);
                Marshaller jaxbMarshaller;
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                int i = 0;
                for (Email email : selectedFolder.getEmails()) {
                    i++;
                    jaxbMarshaller.marshal(email, new File(selectedDir.getAbsolutePath() + "/mail" + i + ".xml"));
                }
            } catch (JAXBException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * Searches for all emails in the selected folder that contain the given
     * pattern.
     *
     * @param input Input in search field.
     * @return a list of all emails that contain the pattern
     */
    @Override
    public List<Email> search(final String input) {
        if (input != null) {
            final ArrayList<Email> filteredMails = new ArrayList<>();
            selectedFolder.getEmails().stream().filter((Email email) -> email.getSubject().toLowerCase().contains(input.trim().toLowerCase())
                    || email.getText().toLowerCase().contains(input.trim().toLowerCase())
                    || email.getReceived().toLowerCase().contains(input.trim().toLowerCase())
                    || email.getSent().toLowerCase().contains(input.trim().toLowerCase())
                    || email.getReceiver().toLowerCase().contains(input.trim().toLowerCase())
                    || email.getSender().toLowerCase().contains(input.trim().toLowerCase())).forEach((email) -> {
                    filteredMails.add(email);
            });
            return filteredMails;
        }
        return null;
    }

    @Override
    public void setEmailStrategy(EmailStrategy strategy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
