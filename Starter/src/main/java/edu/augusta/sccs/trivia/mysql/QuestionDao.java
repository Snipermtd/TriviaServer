package edu.augusta.sccs.trivia.mysql;

import java.util.List;

/*This interface defines which database operations must be available for Questions.
* the actual implementations will be in our TriviaRepository
*/
public interface QuestionDao {
    /*Returns a list containing a requested number of questions matching a difficulty level */
    List<DbQuestion> getQuestionsByDifficulty(int difficulty, int numQuestions);
}
