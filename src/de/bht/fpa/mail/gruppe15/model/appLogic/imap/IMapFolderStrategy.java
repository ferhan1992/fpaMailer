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
 * This class manages the folder strategy for IMAP Folders.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 * 
 */
public class IMapFolderStrategy implements FolderStrategyIF {

    private final Account account;
    private final Store store;

    public IMapFolderStrategy(Account account) {
        this.account = account;
        store = IMapConnectionHelper.connect(account);
    }
    
    /**
     * Loads all relevant content in the directory path of a folder into the
     * folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    @Override
    public void loadContent(final Folder f) {
        if (f != null) {
            if (f.getComponents().isEmpty()) {
                try {
                    javax.mail.Folder topFolder = store.getDefaultFolder();
                    if (!f.getName().contains(account.getName())) {
                        topFolder = store.getFolder(f.getPath());
                    }
                    for (javax.mail.Folder folder : topFolder.list()) {
                        if (folder != null && !folder.getName().isEmpty()) {
                            if (folder.getName().contains("Gmail")) {
                                for (javax.mail.Folder gmailfolder : folder.list()) {
                                    Folder newFolder = new Folder(new File(gmailfolder.getFullName()), gmailfolder.list().length > 0);
                                    newFolder.setPath(gmailfolder.getFullName());
                                    f.addComponent(newFolder);
                                }
                            } else {
                                Folder newFolder = new Folder(new File(folder.getFullName()), folder.list().length > 0);
                                newFolder.setPath(folder.getFullName());
                                f.addComponent(newFolder);
                            }
                        }
                    }
                } catch (MessagingException ex) {
                    Logger.getLogger(IMapFolderStrategy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
