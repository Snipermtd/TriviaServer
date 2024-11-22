package edu.augusta.sccs.trivia.mysql;

/*This interface defines which database operations must be available for QuestionResponse.
 * the actual implementations will be in our TriviaRepository
 */
public interface QuestionResponseDao {

    /*save a player's response to the database*/
    void save (DbQuestionResponse response);
}
