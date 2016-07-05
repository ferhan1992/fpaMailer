    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.gruppe15.model.appLogic.imap;

import de.bht.fpa.mail.gruppe15.model.appLogic.FolderStrategyIF;
import de.bht.fpa.mail.gruppe15.model.data.Account;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Admin
 */
public class IMapFolderStrategy implements FolderStrategyIF {

    private final Account account;
    private final Store store;

    public IMapFolderStrategy(Account account) {
        this.account = account;
        store = IMapConnectionHelper.connect(account);
    }

    @Override
    public void loadContent(Folder f) {
        if (f != null) {
            if (f.getComponents().isEmpty()) {
                try {
                    for (javax.mail.Folder folder : store.getFolder(f.getPath()).list()) {
                        Folder newFolder = new Folder(new File(folder.getName()), folder.list().length > 0);
                        newFolder.setPath(folder.getFullName());
                        f.addComponent(newFolder);
                    }
                } catch (MessagingException ex) {
                    Logger.getLogger(IMapFolderStrategy.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}
