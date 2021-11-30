import java.util.*;

public class Solver {

    private int size;
    private Board startState;

    public Solver(int size, Board startState) {
        this.size = size;
        this.startState = startState;
    }

    /**
     * Should return a tuple of [#expanded nodes, #explored nodes, result node]
     */
    public Solution solve(String heuristic) {
        Board board = new Board(startState);
        board.setHeuristic(heuristic);

        if(!board.isSolvable()) {
            return new Solution(-1, -1, null);
        }

        int explored = 0, expanded = 0;
        Set<Node> closedList = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        HashMap<Board, Integer> minMoves = new HashMap<>();

        Node startNode = new Node(board);
        minMoves.put(startNode.getBoard(), 0);
        pq.add(startNode);
        explored++;

        while(!pq.isEmpty()) {
            Node cur = pq.remove();
            closedList.add(cur);
            expanded++;

            if(cur.getBoard().hasReachedGoal()) {
                startNode = cur;
                break;
            }

            ArrayList<Node> neighbours = cur.getNeighbours();
            for(Node nxt: neighbours) {
                if (!closedList.contains(nxt)) {
                    Integer minSoFar = minMoves.get(nxt.getBoard());
                    if(minSoFar==null || nxt.getMoves()<minSoFar) {
                        minMoves.put(nxt.getBoard(), nxt.getMoves());
                        pq.add(nxt);
                        explored++;
                    }
                }
            }
        }

        return new Solution(expanded, explored, startNode);
    }
}
