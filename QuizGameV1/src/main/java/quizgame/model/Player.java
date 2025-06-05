package quizgame.model;

public class Player extends User implements Comparable<Player> {
    private int score;

    public Player(int userId, String name, String password, Role role, int score) {
        super(userId, name, password, role);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.score, this.score);
    }

}
