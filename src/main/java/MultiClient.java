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
                p1.broadcast("Waiting for player 2 to make a move...");
            }
        });

        player2InputFuture.whenComplete((result, exception) -> {
            if (!player1InputFuture.isDone()) {
                p2.broadcast("Waiting for player 2 to make a move...");
            }
        });

        Option moveP1 = player1InputFuture.join();
        Option moveP2 = player2InputFuture.join();

        broadcastAll("Player 1 chose: " + moveP1 + " and Player 2 chose: " + moveP2);

        if (moveP1.equals(moveP2)) {
            broadcastAll("It's a tie..");
        } else if (moveP1.defeats(moveP2)) {
            broadcastAll("Player 1 wins.");
            p1.user.wonGame();
            p2.user.lostGame();
        } else {
            broadcastAll("Player 2 wins.");
            p1.user.lostGame();
            p2.user.wonGame();
        }

        p1.stopClient();
        p2.stopClient();

    }

    public void broadcastAll(String msg) {
        p1.broadcast(msg);
        p2.broadcast(msg);
    }

}
