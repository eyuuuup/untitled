import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class MultiClient {
    private final Map<String, String> d;
    private final ClientHandler p1;
    private final ClientHandler p2;
    private final ExecutorService executorServiceGameHandling;

    public MultiClient(ClientHandler p1, ClientHandler p2, ExecutorService executorServiceGameHandling) {
        this.p1 = p1;
        this.p2 = p2;
        this.d = new HashMap<>();
        this.executorServiceGameHandling = executorServiceGameHandling;
        d.put("rock", "scissors");
        d.put("scissors", "paper");
        d.put("paper", "rock");
    }

    public void runGame() {
        CompletableFuture<String> player1InputFuture = CompletableFuture.supplyAsync(() -> {
            p1.broadcast("Make your move.");
            return p1.receive();
        }, executorServiceGameHandling);

        CompletableFuture<String> player2InputFuture = CompletableFuture.supplyAsync(() -> {
            p2.broadcast("Make your move.");
            return p2.receive();
        }, executorServiceGameHandling);

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

        String moveP1 = player1InputFuture.join();
        String moveP2 = player2InputFuture.join();

        broadcastAll("Player 1 chose: " + moveP1 + " and Player 2 chose: " + moveP2);

        if (moveP1.equals(moveP2)) {
            broadcastAll("It's a tie..");
        } else if (moveP1.equals(d.get(moveP2))) {
            broadcastAll("Player 1 wins.");
        } else {
            broadcastAll("Player 2 wins.");
        }

        p1.stopClient();
        p2.stopClient();

    }

    public void broadcastAll(String msg) {
        p1.broadcast(msg);
        p2.broadcast(msg);
    }

}
