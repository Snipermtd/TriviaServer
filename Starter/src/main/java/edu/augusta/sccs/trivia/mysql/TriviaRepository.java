package edu.augusta.sccs.trivia.mysql;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.util.ArrayList;
import java.util.List;

/*Concrete implementation of our DAO interfaces*/
public class TriviaRepository implements PlayerDao, QuestionDao, QuestionResponseDao {

    //hibernate manages database connections with SessionFactory
    private final List<SessionFactory> sessionFactories;

    //Allows our trivia repository to interact with different database configurations
    //there is probably a better way to add configurations as we scale to 10,000,000 users but for 2 configs ...
    public TriviaRepository() {

        this.sessionFactories = new ArrayList<>();
        sessionFactories.add(new Configuration()
                .configure("/hibernate.cfg1.xml")
                .buildSessionFactory());
        sessionFactories.add(new Configuration()
                .configure("/hibernate.cfg2.xml")
                .buildSessionFactory());
    }

    /*Returns the correct SessionFactory based on the players uuid*/
    private SessionFactory selectSessionFactoryByPlayer(DbPlayer dbPlayer){
        int playerHash = Math.abs(dbPlayer.getUuid().hashCode()); //hash the uuid and store the result
        int index = playerHash % sessionFactories.size(); // get the correct sessionFactory with the hashed uuid

        return sessionFactories.get(index);
    }

    /*Concrete implementation of our database interactions defined in our DAOs*/
    @Override
    public List<DbQuestion> getQuestionsByDifficulty(int difficulty, int numQuestions) {
        //todo: even though we know sessionFactories[0] is valid from our constructor I still don't like hardcoding an array value here
        // add try/catch?
        Session session = sessionFactories.get(0).openSession(); //Open our connection to the database
        session.beginTransaction();  // Ensures atomicity of database operations
        CriteriaBuilder builder = session.getCriteriaBuilder(); // Creates type-safe queries, prevents sql injection
        CriteriaQuery<DbQuestion> cq = builder.createQuery(DbQuestion.class); // cq is a CriteriaQuery object that contains DbQuestions
        Root<DbQuestion> root = cq.from(DbQuestion.class); // tells cs which table to query
        cq.select(root); // select all columns
        cq.where(builder.equal(root.get("difficulty"), difficulty)); // where difficulty matches our parameter
        List<DbQuestion> questions =
                session.createQuery(cq).setMaxResults(numQuestions).getResultList(); // execute our search and limit the results to our numQuestions
        session.getTransaction().commit(); // commit the transaction
        session.close(); // close the session
        return questions; // return result of the operation
    }

    @Override
    public DbQuestion getQuestion(String uuid){

        Session session = sessionFactories.get(0).openSession(); //Open our connection to the database
        session.beginTransaction();  // Ensures atomicity of database operations
        CriteriaBuilder builder = session.getCriteriaBuilder(); // Creates type-safe queries, prevents sql injection
        CriteriaQuery<DbQuestion> cq = builder.createQuery(DbQuestion.class); // cq is a CriteriaQuery object that contains DbQuestions
        Root<DbQuestion> root = cq.from(DbQuestion.class); // tells cs which table to query
        cq.select(root); // select all columns
        cq.where(builder.equal(root.get("uuid"), uuid.toString())); // where difficulty matches our parameter
        DbQuestion question = session.createQuery(cq).getSingleResult(); // execute our search
        session.getTransaction().commit(); // commit the transaction
        session.close(); // close the session
        return question; // return result of the operation
    }


    @Override
    public DbPlayer findByUuid(String uuid) {
        // Implementation
        return null;
    }

    @Override
    public void save(DbPlayer player) {
        // Implementation
    }

    @Override
    public void save(DbQuestionResponse response) {
        Session session = sessionFactories.get(0).openSession(); //Open our connection to the database
        session.beginTransaction();  // Ensures atomicity of database operations

        session.persist(response); // saves response to the database.

        session.getTransaction().commit(); // commit the transaction
        session.close(); // close the session
    }
}
