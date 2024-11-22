package edu.augusta.sccs.trivia.mysql;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public abstract class AbstractDbConfig {
    protected SessionFactory sessionFactory;

    public AbstractDbConfig(String configFile) {
        Configuration hibernateConfig = new Configuration();
        hibernateConfig.configure(configFile);
        hibernateConfig.addAnnotatedClass(DbQuestion.class);
        hibernateConfig.addAnnotatedClass(DbPlayer.class);
        hibernateConfig.addAnnotatedClass(DbQuestionResponse.class);

        sessionFactory = hibernateConfig.buildSessionFactory();
    }

}

