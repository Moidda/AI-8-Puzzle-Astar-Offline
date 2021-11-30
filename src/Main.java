import java.io.FileInputStream;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new FileInputStream("input.txt"));
            int n = sc.nextInt();

            Board board = new Board(n);
            board.readBoard(sc);

            Solver solver;
            Solution solution;

            solver = new Solver(n, board);
            solution = solver.solve(Board.HAMMING);

            if(solution.explored==-1) {
                System.out.println("Unsolvable");
                return;
            }

            System.out.println("Explored   : " + solution.explored);
            System.out.println("Expanded   : " + solution.expanded);
            System.out.println("Total moves: " + solution.goalNode.getMoves());
            System.out.println("");

            solution = solver.solve(Board.MANHATTAN);
            System.out.println("Explored   : " + solution.explored);
            System.out.println("Expanded   : " + solution.expanded);
            System.out.println("Total moves: " + solution.goalNode.getMoves());
            System.out.println("");

            solution = solver.solve(Board.LINEAR_CONFLICT);
            System.out.println("Explored   : " + solution.explored);
            System.out.println("Expanded   : " + solution.expanded);
            System.out.println("Total moves: " + solution.goalNode.getMoves());
            System.out.println("");

            System.out.println(Node.getAncestors(solution.goalNode));
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
