import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class MultiClient {
    private final ClientHandler p1;
    private final ClientHandler p2;
    private final ExecutorService executorServiceGameHandling;

    public MultiClient(ClientHandler p1, ClientHandler p2, ExecutorService executorServiceGameHandling) {
        this.p1 = p1;
        this.p2 = p2;
        this.executorServiceGameHandling = executorServiceGameHandling;
    }

    public void runGame() {
        CompletableFuture<Option> player1InputFuture = CompletableFuture.supplyAsync(p1::receiveMove, executorServiceGameHandling);

        CompletableFuture<Option> player2InputFuture = CompletableFuture.supplyAsync(p2::receiveMove, executorServiceGameHandling);

        player1InputFuture.whenComplete((result, exception) -> {
            if (!player2InputFuture.isDone()) {
                p1.broadcast("Waiting for " + p2.user.username + " to make a move...");
            }
        });

        player2InputFuture.whenComplete((result, exception) -> {
            if (!player1InputFuture.isDone()) {
                p2.broadcast("Waiting for " + p1.user.username + " to make a move...");
            }
        });

        Option moveP1 = player1InputFuture.join();
        Option moveP2 = player2InputFuture.join();

        broadcastAll(p1.user.username + " chose: " + moveP1 + " and " + p2.user.username + " chose: " + moveP2);

        if (moveP1.equals(moveP2)) {
            broadcastAll("It's a tie..");
            p1.user.tiedGame();
            p2.user.tiedGame();
        } else if (moveP1.defeats(moveP2)) {
            broadcastAll(p1.user.username + " wins.");
            p1.user.wonGame();
            p2.user.lostGame();
        } else {
            broadcastAll(p2.user.username + " wins.");
            p1.user.lostGame();
            p2.user.wonGame();
        }

        broadcastAll("Rematch? y/n");

        CompletableFuture<String> player1RematchFuture = CompletableFuture.supplyAsync(p1::receive, executorServiceGameHandling);

        CompletableFuture<String> player2RematchFuture = CompletableFuture.supplyAsync(p2::receive, executorServiceGameHandling);

        String p1Rematch = player1RematchFuture.join();
        String p2Rematch = player2RematchFuture.join();

        if (p1Rematch.equals("y") && p2Rematch.equals("y")) {
            broadcastAll("Rematched accepted.");
            runGame();
        } else {
            broadcastAll("Rematch declined.");
            p1.stopClient();
            p2.stopClient();
        }



    }

    public void broadcastAll(String msg) {
        p1.broadcast(msg);
        p2.broadcast(msg);
    }

}
