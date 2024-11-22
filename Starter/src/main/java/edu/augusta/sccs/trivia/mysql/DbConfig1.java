package edu.augusta.sccs.trivia.mysql;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DbConfig1 extends AbstractDbConfig{

    public DbConfig1() {
       super("hibernate.cfg1.xml");
    }

    private static DbConfig1 instance = null;

    public static SessionFactory getSessionFactory() {
        if(instance == null) {
            instance = new DbConfig1();
        }
        return instance.sessionFactory;
    }
}
