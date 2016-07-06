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
 *
 * @author Admin
 */
public class IMapEmailStrategy implements EmailStrategyIF {

    private final Account account;
    private final Store store;

    public IMapEmailStrategy(Account account) {
        this.account = account;
        store = IMapConnectionHelper.connect(account);
    }

    @Override
    public void loadEmails(Folder f) {
        if (f != null) {
            f.setContentLoaded();
            try {
                javax.mail.Folder folder = store.getFolder(f.getName());
                if (folder != null) {
                    folder.open(javax.mail.Folder.READ_ONLY);
                    Message[] messages = folder.getMessages();
                    for (Message m : messages) {
                        Email email = IMapEmailConverter.convertMessage(m);
                        if (checkEmailFormat(email)) {
                            f.addEmail(email);
                        }
                    }
                }
            } catch (MessagingException ex) {
                Logger.getLogger(IMapEmailStrategy.class.getName()).log(Level.SEVERE, null, ex);
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
