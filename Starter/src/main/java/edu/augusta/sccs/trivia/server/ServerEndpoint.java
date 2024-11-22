package edu.augusta.sccs.trivia.server;

import edu.augusta.sccs.trivia.*;
import io.grpc.Server;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ServerEndpoint {

    private static final Logger logger = Logger.getLogger(ServerEndpoint.class.getName());


    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new TrivaQuestionImpl())
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

        @Override
        public void getQuestions(QuestionsRequest req, StreamObserver<QuestionsReply> responseObserver) {
            Question q = Question.newBuilder()
                    .setUuid(UUID.randomUUID().toString())
                    .setQuestion("What is the name of the Hurricane that hit Augusta, GA in 2024 causing Augusta University to close?")
                    .setAnswer("Helene")
                    .setDifficulty(1)
                    .setAnswerType(AnswerType.SINGLE_WORD_ANSWER)
                    .build();

            QuestionsReply.Builder builder = QuestionsReply.newBuilder();
            builder.addQuestions(q);
            QuestionsReply reply = builder.build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

    }
}

