package ua.app.classroom.db;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SQLCriterion;
import org.hibernate.service.ServiceRegistry;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.util.Encoding;

import java.util.List;

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
            LOG.error("SessionFactory was not created: ", e);
        }
        return factory;
    }

    static {
        try {
            factory = configureSessionFactory();
        } catch (Exception e) {
            LOG.error("Exception at initialization SessionFactory:", e);
        }
    }

    public static void addUserToDB(User user, String password) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            user.setPassword(Encoding.encodingPassword(password));
            session.save(user);
            session.getTransaction().commit();
            LOG.trace("Added user successfully");
            user.setPassword("");
        } finally {
            session.close();
        }
    }

    public static boolean userIsExist(String fullName) {
        Session session = factory.openSession();
        try {
            Query query = session.createQuery("select count(id) from User where full_name=:fullName");
            query.setString("fullName", fullName);
            LOG.trace("Method findUserByName completed successfully");
            return (Long)query.uniqueResult() != 0;
        } finally {
            session.close();
        }
    }

    public static User getUserByName(String username) {
        Session session = factory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("fullName", username));
            User userDB = (User) criteria.uniqueResult();
            LOG.trace("Method findUserByName completed successfully");
            return userDB;
        } finally {
            session.close();
        }
    }

    public static List<User> getUserList() {
        Session session = factory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.gt("id", 0L));
            return criteria.list();
        } finally {
            session.close();
        }
    }

    public static void deleteUser(User user) {
        Session session = factory.openSession();
        try {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public static void updateUser(User user, String fullName) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("fullName", user.getFullName()));
            User e = (User) criteria.uniqueResult();
            e.setFullName(fullName);
            e.setRole(user.getRole());
            session.saveOrUpdate(e);
            tx.commit();
            LOG.trace("Method updateUser completed successfully");
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOG.debug("Exception in updateUser: ", e);
        } finally {
            session.close();
        }
    }
}
