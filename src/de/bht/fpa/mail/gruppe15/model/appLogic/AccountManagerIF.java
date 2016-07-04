package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Account;
import java.util.List;

/**
 *
 * @author FerhanKaplanseren
 */
public interface AccountManagerIF {

    /**
     * Returns the account with the given name.
     *
     * @return null If no account with this name exists.
     * @param name name of the account
     */
    Account getAccount(final String name);

    /**
     * @return a list of all account names.
     */
    List<Account> getAllAccounts();

    /**
     * Saves the given Account in the data store, if an account with the given
     * name does not exist.
     *
     * @param acc the account that should be saved
     */
    void saveAccount(final Account acc);

    /**
     * Updates the given Account in the data store.
     *
     * @param account the account that should be updated
     * @return true if update was successful.
     */
    boolean updateAccount(final Account account);

}
