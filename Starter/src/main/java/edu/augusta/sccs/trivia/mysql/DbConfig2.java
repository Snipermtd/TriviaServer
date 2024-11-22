package edu.augusta.sccs.trivia.mysql;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DbConfig2 extends AbstractDbConfig{


    public DbConfig2() {
        super("hibernate.cfg2.xml");
    }

    private static DbConfig2 instance = null;

    public static SessionFactory getSessionFactory() {
        if(instance == null) {
            instance = new DbConfig2();
        }
        return instance.sessionFactory;
    }
}
