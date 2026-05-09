public class User {
    public String username;
    private int identifier;
    public int wins;
    public int losses;

    public User(String username) {
        this.username = username;
        this.identifier = setIdentifier(username);
    }

    private int setIdentifier(String username) {
        return username.hashCode();
    }

    public void wonGame() {
        wins = wins + 1;
    }

    public void lostGame() {
        losses = losses + 1;
    }
}
