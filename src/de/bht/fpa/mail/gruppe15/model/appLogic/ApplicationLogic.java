package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.appLogic.imap.IMapEmailStrategy;
import de.bht.fpa.mail.gruppe15.model.appLogic.imap.IMapFolderStrategy;
import de.bht.fpa.mail.gruppe15.model.appLogic.xml.XMLEmailStrategy;
import de.bht.fpa.mail.gruppe15.model.appLogic.xml.XMLFolderStrategy;
import de.bht.fpa.mail.gruppe15.model.data.Account;
import de.bht.fpa.mail.gruppe15.model.data.Email;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the methods which are needed to manage emails and folders.
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class ApplicationLogic implements ApplicationLogicIF {

    /* Declaration of the needed managers for handling folders and emails */
    private final EmailManagerIF emailManager;
    private FolderManagerIF folderManager;
    private final AccountManagerIF accountManager;
    /* Variable of type File holding the root path */
    private static final File ROOT_PATH = new File(System.getProperty("user.dir"));

    /**
     * Constructor of the Class, initializing new instances of FolderManager and
     * EmailManager.
     *
     */
    public ApplicationLogic() {
        folderManager = new FolderManager(ROOT_PATH);
        emailManager = new EmailManager();
        accountManager = new AccountManager();
    }

    /**
     * Get current root folder.
     *
     * @return current root folder.
     */
    @Override
    public Folder getTopFolder() {
        return folderManager.getTopFolder();
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
        folderManager.loadContent(f);
    }

    /**
     * Searches for all emails in the selected folder that contain the given
     * pattern.
     *
     * @param input Input in search field.
     * @return a list of all emails that contain the pattern
     */
    @Override
    public List<Email> search(String input) {
        return emailManager.search(input);
    }

    /**
     * Loads all emails in the directory path of the given folder as objects of
     * the class Email into the folder.
     *
     * @param f the folder into which the emails of the corresponding directory
     * should be loaded
     */
    @Override
    public void loadEmails(final Folder f) {
        emailManager.loadEmails(f);
    }

    /**
     * Changes the root directory of the application, and initializes the folder
     * manager with the new root directory.
     *
     * @param file the path to the directory which was selected as the new root
     * directory of the application.
     */
    @Override
    public void changeDirectory(final File file) {
        if (file != null) {
            folderManager.setTopFolder(new Folder(file, true));
            folderManager.setFolderStrategy(new XMLFolderStrategy());
            emailManager.setEmailStrategy(new XMLEmailStrategy());
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
        emailManager.saveEmails(selectedDir);
    }

    /**
     * Sets a selected account as the new working account, and initializes the
     * folder manager with the top Folder of the account.
     *
     * @param name name of the account which should be set as the current
     * working account.
     */
    @Override
    public void openAccount(final String name) {
        if (name != null) {
            final Account account;
            account = getAccount(name);
            final File file;
            file = new File(account.getTop().getPath());
            folderManager.setTopFolder(account.getTop());
            if (!file.exists()) {
                folderManager.setFolderStrategy(new IMapFolderStrategy(account));
                emailManager.setEmailStrategy(new IMapEmailStrategy(account));
            } else {
                folderManager.setFolderStrategy(new XMLFolderStrategy());
                emailManager.setEmailStrategy(new XMLEmailStrategy());
            }
        }
    }

    /**
     * @return a list of all account names.
     */
    @Override
    public List<String> getAllAccounts() {
        final List<String> allAccounts;
        allAccounts = new ArrayList<>();
        accountManager.getAllAccounts().stream().forEach((x) -> {
            allAccounts.add(x.getName());
        });
        return allAccounts;
    }

    /**
     * @return account with the given name. If no account with this name exists,
     * it returns null.
     * @param name name of the account
     */
    @Override
    public Account getAccount(final String name) {
        return accountManager.getAccount(name);
    }

    /**
     * Saves the given Account in the datastore.
     *
     * @param account the account that should be saved
     * @return true if an account with this name did not exist.
     */
    @Override
    public boolean saveAccount(final Account account) {
        if (account != null) {
            accountManager.saveAccount(account);
            return true;
        }
        return false;
    }

    /**
     * Updates the given Account in the datastore.
     *
     * @param account the account that should be updated
     */
    @Override
    public void updateAccount(final Account account) {
        accountManager.updateAccount(account);
    }
}
