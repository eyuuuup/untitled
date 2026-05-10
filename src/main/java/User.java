public class User {
    public String username;
    public int wins;
    public int ties;
    public int losses;

    public User(String username) {
        this.username = username;
    }

    public void wonGame() {
        wins = wins + 1;
    }

    public void tiedGame () {
        ties = ties + 1;
    }

    public void lostGame() {
        losses = losses + 1;
    }

    public int totalGames() {
        return wins + losses + ties;
    }

    public double winrate() {
        if (totalGames() == 0) {
            return 0.0;
        }

        return  ((double) wins / totalGames()) * 100;
    }
}
