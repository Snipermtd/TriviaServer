package edu.augusta.sccs.trivia.mysql;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/*Concrete implementation of our DAO interfaces*/
public class TriviaRepository implements PlayerDao, QuestionDao, QuestionResponseDao {

    //hibernate manages database connections with SessionFactory
    private final SessionFactory sessionFactory;

    //Allows our trivia repository to interact with different database configurations
    public TriviaRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    /*todo:database shard implementation*/

    /*Concrete implementation of our database interactions defined in our DAOs*/
    @Override
    public List<DbQuestion> getQuestionsByDifficulty(int difficulty, int numQuestions) {
        Session session = sessionFactory.openSession(); //Open our connection to the database
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
        // Implementation
    }
}
