package ua.app.classroom.db;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.jboss.logging.Logger;
import ua.app.classroom.model.User;
import ua.app.classroom.util.Encoding;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;

@ApplicationScoped
public class UserDB {

    private static final Logger LOG = Logger.getLogger(UserDB.class);
    private static SessionFactory factory;

    private static SessionFactory configureSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
                    applySettings(configuration.getProperties()).build();
            factory = configuration.configure().buildSessionFactory(serviceRegistry);
            return factory;
        } catch (HibernateException e) {
            LOG.info("Don't create session: ", e);
        }
        return factory;
    }

    static {
        try {
            factory = configureSessionFactory();
        } catch (Exception e) {
            LOG.info("Exception in static block:", e);
        }
    }

    public void addUser(User user, String password) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            user.setPassword(Encoding.encodingPassword(password));
            session.save(user);
            session.getTransaction().commit();
            user.setPassword("");
        } finally {
            session.close();
        }
    }

    public boolean findUser(User user, String password) {
        Session session = factory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("fullName", user.getFullName()));
            criteria.add(Restrictions.eq("password", Encoding.encodingPassword(password)));
            User userDB = (User) criteria.uniqueResult();
            return userDB != null;
        } finally {
            session.close();
        }
    }

    public Collection<User> getUserList() {
        Session session = factory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("userAlreadySignedUp", true));
            return criteria.list();
        } finally {
            session.close();
        }
    }

    public void updateUser(User user, boolean onlineStatus) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("fullName", user.getFullName()));
            User e = (User) criteria.uniqueResult();
            e.setUserAlreadySignedUp(onlineStatus);
            session.saveOrUpdate(e);
            tx.commit();
        } catch (HibernateException asd) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.debug(asd.getMessage());
        } finally {
            session.close();
        }
    }
}
