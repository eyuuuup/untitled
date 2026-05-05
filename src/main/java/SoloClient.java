import java.util.Optional;
import java.util.Random;

public class SoloClient {

    private final ClientHandler p1;

    public SoloClient(ClientHandler p1) {
        this.p1 = p1;
    }

    public void runGame() {
        p1.broadcast("You ever heard of rock paper scissors?");
        Random r = new Random();

        Option userOption;
        while ((userOption = p1.receiveMove()) != null) {


            Option cpuOption = Option.values()[r.nextInt(Option.values().length)];
            p1.broadcast("I choose " + cpuOption);

            if (cpuOption.equals(userOption)) {
                p1.broadcast("It's a tie..");
            } else if (cpuOption.defeats(userOption)) {
                p1.broadcast("I win.");
            } else {
                p1.broadcast("I lost...");
            }
        }
    }
}
