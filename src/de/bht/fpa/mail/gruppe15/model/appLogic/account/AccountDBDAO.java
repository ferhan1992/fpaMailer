package de.bht.fpa.mail.gruppe15.model.appLogic.account;

import de.bht.fpa.mail.gruppe15.model.data.Account;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * This class defines a dao class, that stores Account objects as serialized
 * data in a given file.
 *
 * @author Simone Strippgen
 */
public class AccountDBDAO implements AccountDAOIF {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("fpa");

    public AccountDBDAO() {
        TestDBDataProvider.createAccounts();
    }

    @Override
    public List<Account> getAllAccounts() {
        try {
            final List<Account> list;
            final EntityManager em;
            em = emf.createEntityManager();
            final Query query;
            query = em.createQuery("SELECT a FROM Account a");
            list = query.getResultList();
            em.close();
            return list;
        } catch (PersistenceException ex) {
            System.out.println(ex.getCause());
        }
        return new ArrayList<>();
    }

    @Override
    public Account saveAccount(final Account acc) {
        if (acc != null) {
            try {
                final EntityManager em;
                em = emf.createEntityManager();
                final EntityTransaction trans;
                trans = em.getTransaction();
                trans.begin();
                em.persist(acc);
                trans.commit();
                em.close();
                return acc;
            } catch (PersistenceException ex) {
                System.out.println(ex.getCause());
            }
        }
        return acc;
    }

    @Override
    public boolean updateAccount(final Account acc) {
        if (acc != null) {
            try {
                final EntityManager em;
                em = emf.createEntityManager();
                final EntityTransaction trans;
                trans = em.getTransaction();
                trans.begin();
                em.merge(acc);
                trans.commit();
                em.close();
                return true;
            } catch (PersistenceException ex) {
                System.out.println(ex.getCause());
            }
        }
        return false;
    }
}
