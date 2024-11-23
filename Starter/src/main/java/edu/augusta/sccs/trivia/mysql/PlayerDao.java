package edu.augusta.sccs.trivia.mysql;

/*This interface defines which database operations must be available for Players.
* the actual implementations will be in our TriviaRepository
*/
public interface PlayerDao {
    /*finds a player when given a String uuid*/
    DbPlayer findByUuid(String uuid);

    /*save a player to the database*/
    void save(DbPlayer player);
}
