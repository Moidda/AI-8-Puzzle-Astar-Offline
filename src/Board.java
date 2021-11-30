import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

public class Board {
    public static String HAMMING = "hamming";
    public static String MANHATTAN = "manhattan";
    public static String LINEAR_CONFLICT = "linear_conflict";

    private int[][] b;
    private String heuristic;
    private int blankCellRow, blankCellCol;

    private int cell_space = -1;


    private int correctRow(int x) {
        if(x == 0) return b.length-1;
        return (x-1) / b.length;
    }

    private int correctColumn(int x) {
        if(x == 0) return b.length-1;
        return (x-1) % b.length;
    }



    public Board(int n) {
        b = new int[n][n];
        this.heuristic = HAMMING;
    }

    public Board(Board board) {
        this.b = new int[board.b.length][board.b.length];
        for(int i = 0; i < board.b.length; i++)
            for(int j = 0; j < board.b.length; j++)
                this.b[i][j] = board.b[i][j];

        this.heuristic = board.heuristic;
        this.blankCellRow = board.blankCellRow;
        this.blankCellCol = board.blankCellCol;
        this.cell_space = board.cell_space;
    }

    public void setHeuristic(String heuristic) {
        this.heuristic = heuristic;
    }

    public Board moveRight() {
        if(blankCellCol+1 == b.length) return this;
        Board ret = new Board(this);
        ret.b[blankCellRow][blankCellCol] = this.b[blankCellRow][blankCellCol+1];
        ret.b[blankCellRow][blankCellCol+1] = this.b[blankCellRow][blankCellCol];
        ret.blankCellCol++;
        return ret;
    }

    public Board moveLeft() {
        if(blankCellCol-1 < 0) return this;
        Board ret = new Board(this);
        ret.b[blankCellRow][blankCellCol] = this.b[blankCellRow][blankCellCol-1];
        ret.b[blankCellRow][blankCellCol-1] = this.b[blankCellRow][blankCellCol];
        ret.blankCellCol--;
        return ret;
    }

    public Board moveDown() {
        if(blankCellRow+1 == b.length) return this;
        Board ret = new Board(this);
        ret.b[blankCellRow][blankCellCol] = this.b[blankCellRow+1][blankCellCol];
        ret.b[blankCellRow+1][blankCellCol] = this.b[blankCellRow][blankCellCol];
        ret.blankCellRow++;
        return ret;
    }

    public Board moveUp() {
        if(blankCellRow-1 < 0) return this;
        Board ret = new Board(this);
        ret.b[blankCellRow][blankCellCol] = this.b[blankCellRow-1][blankCellCol];
        ret.b[blankCellRow-1][blankCellCol] = this.b[blankCellRow][blankCellCol];
        ret.blankCellRow--;
        return ret;
    }

    public int getHamming() {
        int ret = 0;
        for(int i = 0; i < b.length; i++)
            for(int j = 0; j < b.length; j++) {
                if(b[i][j]==0) continue;
                if(correctRow(b[i][j])!=i || correctColumn(b[i][j])!=j)
                    ret++;
            }

        return ret;
    }

    public int getManhattan() {
        int ret = 0;
        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b.length; j++) {
                if(b[i][j]==0) continue;
                ret += Math.abs(correctRow(b[i][j]) - i) + Math.abs(correctColumn(b[i][j]) - j);
            }
        }
        return ret;
    }

    public int getLinearConflict() {
        int rowConflicts = 0;
        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b.length; j++) {
                for(int k = j+1; k < b.length; k++) {
                    if(b[i][j]==0 || b[i][k]==0 || correctRow(b[i][j])!=i || correctRow(b[i][k])!=i) continue;
                    if(correctColumn(b[i][j]) > correctColumn(b[i][k]))
                        rowConflicts++;
                }
            }
        }
//        System.out.println("Row conflicts: " + rowConflicts);
        return getManhattan() + 2*rowConflicts;
    }

    public int getHeuristic() {
        if(heuristic.equalsIgnoreCase(MANHATTAN)) return getManhattan();
        if(heuristic.equalsIgnoreCase(LINEAR_CONFLICT)) return getLinearConflict();
        return getHamming();
    }

    public int getInversionCount() {
        int inversion_count = 0;
        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b.length; j++) {
                if(b[i][j] == 0) continue;
//                System.out.print(b[i][j] + ": ");
                int count = 0;
                int ii = i, jj = j+1;
                while(ii < b.length) {
                    if(jj<b.length && b[ii][jj] == 0) jj++;
                    if(jj == b.length) {
                        jj = 0;
                        ii++;
                    }
                    else {
                        count += b[i][j]>b[ii][jj] ? 1:0;
                        jj++;
                        if(jj == b.length) {
                            jj = 0;
                            ii++;
                        }
                    }
                }
//                System.out.println(count);
                inversion_count += count;
            }
        }
//        System.out.println("Inversion count: " + inversion_count);
        return inversion_count;
    }

    public boolean isSolvable() {
        int inversionCount = getInversionCount();
        if(b.length%2 == 1) return (inversionCount%2 == 0);
        return (b.length-blankCellRow)%2 != inversionCount%2;
    }

    public boolean hasReachedGoal() {
        return getHamming()==0;
    }

    public void readBoard(Scanner sc) {
        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b.length; j++) {
                String in = sc.next();
                if(in.equals("*")) {
                    b[i][j] = 0;
                    blankCellRow = i;
                    blankCellCol = j;
                }
                else {
                    b[i][j] = Integer.parseInt(in);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.deepEquals(b, board.b);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(b);
    }

    @Override
    public String toString() {
        if(cell_space == -1) cell_space = (int)(Math.log10(b.length*b.length) + 1) ;
        String ret = "";
        int hor_boundary = b.length*cell_space + b.length+1;

        for(int i = 0; i < hor_boundary; i++) ret += "-";
        ret += "\n";

        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b.length; j++) {
                ret += "|";
                int space_count = b[i][j]!=0 ? cell_space - (int)(Math.log10(b[i][j]) + 1) : cell_space - 1;
                while(space_count > 0) {
                    ret += " ";
                    space_count--;
                }
                if(b[i][j] == 0) ret += "*";
                else ret += String.valueOf(b[i][j]);
            }
            ret += "|\n";

            for(int j = 0; j < hor_boundary; j++) ret += "-";
            ret += "\n";
        }
        return ret;
    }
}
