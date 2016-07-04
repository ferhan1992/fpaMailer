package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.appLogic.account.AccountDAOIF;
import de.bht.fpa.mail.gruppe15.model.data.Account;
import java.util.List;

/*
 * This is the class that manages
 * accounts.
 * 
 * @author Simone Strippgen
 */
public class AccountManager implements AccountManagerIF{

    private AccountDAOIF accountDB;
    private List<Account> accountList;


    public AccountManager() {
	// hier kommt Ihr Code hinein
    }

    /**
     * Returns the account with the given name.
     * @return null If no account with this name exists.
     * @param name  name of the account 
     */
    @Override
    public Account getAccount(final String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return a list of all account names.
     */
    @Override
    public List<Account> getAllAccounts() {   
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Saves the given Account in the data store, if an account
     * with the given name does not exist.
     * @param acc  the account that should be saved
     */
    @Override
    public void saveAccount(final Account acc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Updates the given Account in the data store.
     * @param account  the account that should be updated
     * @return true if update was successful.
     */
    @Override
    public boolean updateAccount(final Account account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
