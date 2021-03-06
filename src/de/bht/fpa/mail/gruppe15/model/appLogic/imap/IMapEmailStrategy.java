/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.gruppe15.model.appLogic.imap;

import de.bht.fpa.mail.gruppe15.model.appLogic.EmailStrategyIF;
import de.bht.fpa.mail.gruppe15.model.data.Account;
import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * This class manages the email strategy for IMAP Emails.
 * 
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 * 
 */
public class IMapEmailStrategy implements EmailStrategyIF {

    private final Account account;
    private final Store store;

    public IMapEmailStrategy(final Account account) {
        this.account = account;
        store = IMapConnectionHelper.connect(account);
    }
    
    /**
     * Loads all emails in the directory path of a folder into the given folder.
     *
     * @param f the folder into which the content of emails should be loaded
     */
    @Override
    public void loadEmails(final Folder f) {
        if (f != null) {
            try {
                if (store.getFolder(f.getPath()).exists() && !f.getContentLoaded()) {
                    final javax.mail.Folder folder;
                    folder = store.getFolder(f.getPath());
                    if (folder != null) {
                        folder.open(javax.mail.Folder.READ_ONLY);
                        final Message[] messages;
                        messages = folder.getMessages();
                        for (final Message m : messages) {
                            final Email email;
                            email = IMapEmailConverter.convertMessage(m);
                            if (checkEmailFormat(email)) {
                                f.addEmail(email);
                            }
                        }
                        folder.close(false);
                        f.setContentLoaded();
                    }
                }
            } catch (final MessagingException ex) {
                Logger.getLogger(IMapEmailStrategy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Method to check the passed email if it contains all the needed
     * variables to be accepted.
     *
     * @param email The Email which gets checked.
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
