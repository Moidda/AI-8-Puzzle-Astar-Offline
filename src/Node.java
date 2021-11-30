import java.util.ArrayList;
import java.util.Objects;

public class Node implements Comparable<Node> {
    private final Board board;
    private final int moves;
    private final Node prv;

    private final int heuristic;

    public static String getAncestors(Node cur) {
        if(cur == null) return "";
        return getAncestors(cur.getPrv()) + "\n" + cur.getBoard().toString();
    }

    public Node(Board startState) {
        this.board = startState;
        this.moves = 0;
        this.prv = null;

        this.heuristic = board.getHeuristic();
    }

    public Node(Board board, int moves, Node prv) {
        this.board = board;
        this.moves = moves;
        this.prv = prv;

        this.heuristic = board.getHeuristic();
    }

    public Board getBoard() {
        return board;
    }

    public int getMoves() {
        return moves;
    }

    public Node getPrv() {
        return prv;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public ArrayList<Node> getNeighbours() {
        ArrayList<Node> ret = new ArrayList<>();

        Board right = board.moveRight();
        Board left = board.moveLeft();
        Board up = board.moveUp();
        Board down = board.moveDown();

        if(!right.equals(board)) ret.add(new Node(right, moves+1, this));
        if(!left.equals(board)) ret.add(new Node(left, moves+1, this));
        if(!up.equals(board)) ret.add(new Node(up, moves+1, this));
        if(!down.equals(board)) ret.add(new Node(down, moves+1, this));

        return ret;
    }

    @Override
    public int compareTo(Node o) {
        if(moves+heuristic > o.moves+o.heuristic) return 1;
        if(moves+heuristic < o.moves+o.heuristic) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return moves == node.moves && board.equals(node.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, moves);
    }

    @Override
    public String toString() {
        return
                "Moves     = " + moves + "\n" +
                "Heuristic = " + heuristic + "\n" +
                "Total     = " + (moves+heuristic) + "\n" +
                board + "\n";
    }
}
