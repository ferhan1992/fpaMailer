package de.bht.fpa.mail.gruppe15.model.appLogic.account;

import de.bht.fpa.mail.gruppe15.model.data.Account;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
        final List<Account> list;
        final EntityManager em;
        em = emf.createEntityManager();
        final Query query;
        query = em.createQuery("SELECT k FROM Kunde k");
        list = query.getResultList();
        em.close();
        return list;
    }

    @Override
    public Account saveAccount(final Account acc) {
        if (acc != null) {
            final EntityManager em;
            em = emf.createEntityManager();
            final EntityTransaction trans;
            trans = em.getTransaction();
            trans.begin();
            em.persist(acc);
            trans.commit();
            em.close();
        }
        return acc;
    }

    @Override
    public boolean updateAccount(final Account acc) {
        if (acc != null) {
            final EntityManager em;
            em = emf.createEntityManager();
            final EntityTransaction trans;
            trans = em.getTransaction();
            trans.begin();
            em.merge(acc);
            trans.commit();
            em.close();
            return true;
        }
        return false;
    }
}
