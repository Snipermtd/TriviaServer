package edu.augusta.sccs.trivia.server;

import edu.augusta.sccs.trivia.*;
import edu.augusta.sccs.trivia.mysql.DbQuestion;
import edu.augusta.sccs.trivia.mysql.TriviaRepository;
import io.grpc.Server;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ServerEndpoint {

    private static final Logger logger = Logger.getLogger(ServerEndpoint.class.getName());
    private Server server;

    /*Provide the repository to the endpoint to handle all database operations*/
    private final TriviaRepository triviaRepository;

    /*database configurations are handled in TriviaRepository, we just ensure our server has one*/
    public ServerEndpoint(){
        this.triviaRepository =  new TriviaRepository();
    }


    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                //add our repository to the service
                .addService(new TrivaQuestionImpl(triviaRepository))
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    if (server != null) {
                        server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }


    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            System.out.println("going to await termination");
            server.awaitTermination();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        final ServerEndpoint server = new ServerEndpoint();
        server.start();
        server.blockUntilShutdown();
    }

    static class TrivaQuestionImpl extends TriviaQuestionsGrpc.TriviaQuestionsImplBase {

        // Repository instance for database operations
        private final TriviaRepository triviaRepository;

        // Constructor injection of repository
        public TrivaQuestionImpl(TriviaRepository triviaRepository){
            this.triviaRepository = triviaRepository;
        }

        /* Handles client requests for questions
         * Gets questions from database and converts them to gRPC format */
        @Override
        public void getQuestions(QuestionsRequest req, StreamObserver<QuestionsReply> responseObserver) {
            // Get questions from database matching request criteria
            List<DbQuestion> dbQuestions = triviaRepository.getQuestionsByDifficulty(
                    req.getDifficulty(),
                    req.getNumberOfQuestions()
            );


            // Convert database questions to gRPC format
            QuestionsReply.Builder builder = QuestionsReply.newBuilder();
            for (DbQuestion dbQuestion : dbQuestions) {
                Question q = Question.newBuilder()
                        .setUuid(dbQuestion.getUuid().toString())
                        .setQuestion(dbQuestion.getQuestion())
                        .setAnswer(dbQuestion.getAnswer())
                        .setDifficulty(dbQuestion.getDifficulty())
                        .setAnswerType(dbQuestion.getAnswerType())
                        .build();
                builder.addQuestions(q);
            }

            // Send response back to client
            QuestionsReply reply = builder.build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}

