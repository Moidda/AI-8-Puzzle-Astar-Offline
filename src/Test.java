import java.io.*;
import java.util.*;

/**
 * https://www.udebug.com/LOJ/1139
 * Passes all the tests.
 * Paste the input in "input.txt".
 */

public class Test {
    public static void replaceInFile(File file) throws IOException {
        Scanner scanner = new Scanner(new FileInputStream(file));
        FileWriter fileWriter = new FileWriter("./new_input.txt");

        String line = scanner.nextLine();
        fileWriter.write(line + "\n");

        while(scanner.hasNextLine()) {
            line = scanner.nextLine();
            String toWrite = "";
            for(int i = 0; i < line.length(); i++) {
                if(line.charAt(i) == '0') toWrite += "*";
                else toWrite += line.charAt(i);
            }
            toWrite += "\n";
            fileWriter.write(toWrite);
        }
        fileWriter.close();
    }

    public static void main(String[] args) throws Exception {
        replaceInFile(new File("input.txt"));
        Scanner scanner = new Scanner(new FileInputStream("new_input.txt"));

        FileWriter fileWriter = new FileWriter("./output.txt");

        int tc = scanner.nextInt();
        for(int nc = 1; nc <= tc; nc++) {
            Board board = new Board(3);
            board.readBoard(scanner);

            Solver solver = new Solver(3, board);
            Solution solution = solver.solve(Board.LINEAR_CONFLICT);

            fileWriter.write("Case " + nc + ": ");
            if(solution.goalNode == null) fileWriter.write("impossible\n");
            else fileWriter.write(solution.goalNode.getMoves() + "\n");
        }

        fileWriter.close();
    }
}
