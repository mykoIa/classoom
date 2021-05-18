package ua.app.classroom.db;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import ua.app.classroom.model.User;
import ua.app.classroom.util.Encoding;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.annotation.WebListener;

@ApplicationScoped
@WebListener
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

    public void addUserToDB(User user, String password) {
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

    public boolean findUserByName(User user) {
        Session session = factory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("fullName", user.getFullName()));
            User userDB = (User) criteria.uniqueResult();
            LOG.trace("Method findUserByName completed successfully");
            return userDB != null;
        } finally {
            session.close();
        }
    }

    public User findUserByName(String username) {
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

    public boolean findUser(User user, String password) {
        Session session = factory.openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("fullName", user.getFullName()));
            criteria.add(Restrictions.eq("password", Encoding.encodingPassword(password)));
            User userDB = (User) criteria.uniqueResult();
            LOG.trace("Method findUser completed successfully");
            return userDB != null;
        } finally {
            session.close();
        }
    }
}
