package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.appLogic.account.AccountDAOIF;
import de.bht.fpa.mail.gruppe15.model.appLogic.account.AccountDBDAO;
import de.bht.fpa.mail.gruppe15.model.appLogic.account.AccountFileDAO;
import de.bht.fpa.mail.gruppe15.model.data.Account;
import java.util.List;

/*
 * This is the class that manages
 * accounts.
 * 
 * @author Simone Strippgen
 */
public class AccountManager implements AccountManagerIF {

    private final AccountDAOIF accountDB;
    private final List<Account> accountList;

    public AccountManager() {
        //accountDB = new AccountFileDAO();
        accountDB = new AccountDBDAO();
        accountList = accountDB.getAllAccounts();
    }

    /**
     * Returns the account with the given name.
     *
     * @return null If no account with this name exists.
     * @param name name of the account
     */
    @Override
    public Account getAccount(final String name) {
        if (name != null) {
            for (Account acc : accountList) {
                if (acc.getName().equals(name)) {
                    return acc;
                }
            }
        }
        return null;
    }

    /**
     * @return a list of all account names.
     */
    @Override
    public List<Account> getAllAccounts() {
        return accountList;
    }

    /**
     * Saves the given Account in the data store, if an account with the given
     * name does not exist.
     *
     * @param acc the account that should be saved
     */
    @Override
    public void saveAccount(final Account acc) {
        if (acc != null && checkAccountExistence(acc)) {
            accountDB.saveAccount(acc);
            this.accountList.add(acc);
        } else if (!checkAccountExistence(acc)) {
            System.out.println("Account already exists.");
        }
    }

    private boolean checkAccountExistence(final Account acc) {
        for (Account account : accountList) {
            if (account.getName().equals(acc.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the given Account in the data store.
     *
     * @param account the account that should be updated
     * @return true if update was successful.
     */
    @Override
    public boolean updateAccount(final Account account) {
        return accountDB.updateAccount(account);
    }
}
