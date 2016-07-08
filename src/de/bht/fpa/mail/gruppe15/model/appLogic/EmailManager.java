package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.controller.MainWindowController;
import de.bht.fpa.mail.gruppe15.model.appLogic.xml.XMLEmailStrategy;
import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private EmailStrategyIF emailStrategy;

    public EmailManager() {
        emailStrategy = new XMLEmailStrategy();
    }

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
            emailStrategy.loadEmails(f);
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
                final JAXBContext jaxbContext;
                jaxbContext = JAXBContext.newInstance(Email.class);
                final Marshaller jaxbMarshaller;
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                int i = 0;
                for (final Email email : selectedFolder.getEmails()) {
                    i++;
                    jaxbMarshaller.marshal(email, new File(selectedDir.getAbsolutePath() + "/mail" + i + ".xml"));
                }
            } catch (final JAXBException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
            final ArrayList<Email> filteredMails;
            filteredMails = new ArrayList<>();
            selectedFolder.getEmails().stream().filter((final Email email) -> email.getSubject().toLowerCase().contains(input.trim().toLowerCase())
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
    public void setEmailStrategy(final EmailStrategyIF strategy) {
        this.emailStrategy = strategy;
        loadEmails(selectedFolder);
    }
}
