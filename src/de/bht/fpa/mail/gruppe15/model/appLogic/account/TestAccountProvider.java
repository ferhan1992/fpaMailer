package de.bht.fpa.mail.gruppe15.model.appLogic.account;

import de.bht.fpa.mail.gruppe15.model.data.Account;
import de.bht.fpa.mail.gruppe15.model.data.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the name of the directory where the testdata for the
 * accounts can be found and is able to produce some test accounts.
 *
 * @author Simone Strippgen
 */
class TestAccountProvider {

//  directory (relative to project) where the corresponding email folders can be found
    public static final File TESTDATA_HOME = new File("TestData");

    /**
     * Returns a list of accounts, with top folder paths which refer to the
     * folder with the testdata.
     */
    public static List<Account> createAccounts() {
        final List<Account> accounts;
        accounts = new ArrayList<>();
        Account acc;
        acc = new Account("Walter", "smtp.gmail.com", "walter@gmail.com", "walter");
        //Sets the path to the corresponding email folder
        Folder folder;
        folder = new Folder(new File(TESTDATA_HOME, "Walter"), true);
        acc.setTop(folder);
        accounts.add(acc);
        acc = new Account("Erna", "smtp.gmail.com", "erna@gmail.com", "erna");
        folder = new Folder(new File(TESTDATA_HOME, "Erna"), true);
        acc.setTop(folder);
        accounts.add(acc);
        acc = new Account("Anna", "smtp.gmail.com", "anna@gmail.com", "anna");
        folder = new Folder(new File(TESTDATA_HOME, "Anna"), true);
        acc.setTop(folder);
        accounts.add(acc);
        acc = new Account("Google-Test", "smtp.gmail.com", "huber.fpa@gmail.com", "ernaHuberFpa");
        accounts.add(acc);
        return accounts;
    }
}
