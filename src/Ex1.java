import java.io.*;
import java.util.Scanner;
import java.util.Vector;


public class Ex1 {

    /**
     * This function find where is the empty panel in the board(for the initial of the state).
     *
     * @param board - state board.
     * @return - arr[0] -> i1, arr[1] -> j1, arr[2] -> i2, arr[3] -> j2.
     */
    public static int[] emptyPanel(int[][] board) {
        int[] ans = new int[4];
        boolean flag = false;
        boolean flag2 = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    if (flag == false) {
                        ans[0] = i;
                        ans[1] = j;
                        flag = true;
                    } else {
                        ans[2] = i;
                        ans[3] = j;
                        flag2 = true;
                    }
                }
            }
        }
        if (flag2 == false) {
            ans[2] = -1;
            ans[3] = -1;
        }
        return ans;
    }


    public static void main(String[] args) throws IOException {

        boolean withOpen = false;
        boolean withTime = false;
        String algorithm = "";
        int row;
        int col;
        String[] rowAndColStr;


        File file = new File("input.txt");
        Scanner scanner = new Scanner(file);

        //check which algorithms to use.
        algorithm = scanner.nextLine();

        //check if it is with time or not.
        if (scanner.nextLine().charAt(0) == 'w') {
            withTime = true;
        }

        //check if it is with print of the open list or not.
        if (scanner.nextLine().charAt(0) == 'w') {
            withOpen = true;
        }

        //get the size of the array that will be the start and the gaol state board.
        rowAndColStr = scanner.nextLine().split("x");
        row = Integer.parseInt(rowAndColStr[0]);
        col = Integer.parseInt(rowAndColStr[1]);
        int[][] startBoard = new int[row][col];
        int i = 0;
        int j = 0;
        String line = scanner.nextLine();


        //init the start state board.
        while (!line.contains("G")) {
            String[] arrStr = line.split(",");
            for (int k = 0; k < arrStr.length; k++) {
                if (arrStr[k].equals("_")) {
                    arrStr[k] = "0";
                }
                startBoard[i][j++] = Integer.parseInt(arrStr[k]);
            }
            j = 0;
            i++;
            line = scanner.nextLine();
        }

        //init the goal state board.
        int[][] goalBoard = new int[row][col];
        i = 0;
        j = 0;
        line = scanner.nextLine();
        while (i < goalBoard.length) {
            String[] arrStr = line.split(",");
            for (int k = 0; k < arrStr.length; k++) {
                if (arrStr[k].equals("_")) {
                    arrStr[k] = "0";
                }
                goalBoard[i][j++] = Integer.parseInt(arrStr[k]);
            }
            j = 0;
            i++;
            if (scanner.hasNext()) {
                line = scanner.nextLine();
            }
        }
        scanner.close();


        // init all the puzzle game and run the program.
        int[] emptyPanelStart = emptyPanel(startBoard);
        State start = new State(startBoard, 0, null, goalBoard, emptyPanelStart[0], emptyPanelStart[1], emptyPanelStart[2], emptyPanelStart[3]);
        int[] emptyPanelGoal = emptyPanel(goalBoard);
        State goal = new State(goalBoard, 0, null, goalBoard, emptyPanelGoal[0], emptyPanelGoal[1], emptyPanelGoal[2], emptyPanelGoal[3]);

        Vector<State> v = new Vector<>();
        v.add(goal);

        //create output file.
        //initialize the ans for the output file
        String ans = "";
        PuzzleGameAlgo puzzle = new PuzzleGameAlgo(start, v, withOpen);
        double startTime = System.nanoTime();
        ans = puzzle.collAlgorithm(algorithm);
        double stopTime = System.nanoTime();
        if (withTime == true) {
            ans+= ((stopTime - startTime) / 1000000000) + " second";
        }
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(ans);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

