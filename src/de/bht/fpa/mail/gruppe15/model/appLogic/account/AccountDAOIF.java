package de.bht.fpa.mail.gruppe15.model.appLogic.account;

import de.bht.fpa.mail.gruppe15.model.data.Account;
import java.util.List;

/*
 * This is the interface for DAO classes that manage
 * account infos in a data store.
 * 
 * @author Simone Strippgen
 */
public interface AccountDAOIF {

    /**
     * @return a list of all stored accounts.
     */
    List<Account> getAllAccounts();

    /**
     * Saves the given Account in the data store.
     *
     * @param acc the account that should be saved
     * @return account which was stored in the data store
     */
    Account saveAccount(final Account acc);

    /**
     * Updates the given Account in the data store.
     *
     * @param acc the account that should be updated
     * @return true if update was successful
     */
    boolean updateAccount(final Account acc);

}
