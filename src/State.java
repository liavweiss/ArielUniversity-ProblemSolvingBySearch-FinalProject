import java.util.ArrayList;

/**
 * This class represents a state,the state will be used by us to solve the puzzle tile-NxM game.
 *
 * @Author Liav Weiss.
 */
public class State implements Comparable<State> {

    /**
     * Each state will have several fields:
     *
     * @param board - The game board in the given state.
     * @param cost - The price it cost us to get from the initial state to the new state.
     * @param pre - Which "state" he came from.
     * @param strPath - string ot the path from start to the goal state.
     * @param stateID - Each situation will have its own ID  to use in  the priority queue,
     * * in a situation where two states have equality in their costs.
     * @param goalStateBoard - the target State, use for priority queue and Manhattan Distance heuristic function.
     * @param tag - for IDA* algorithm(it symbolizes whether the state is marked as "out" or not).
     * @param i1 - The first row that containing an empty panel.
     * @param j1 - The first col that containing an empty panel.
     * @param i2 - The second row that containing an empty panel.
     * @param j2 - The second col that containing an empty panel.
     */
    private int[][] board;
    private int cost;
    private State pre;
    private String strPath;
    private static int stateID = 0;
    private int[][] goalStateBoard;
    private String tag;
    private int i1;
    private int j1;
    private int i2;
    private int j2;


    /**
     * Constructor for a game with one empty panel.
     */
    public State(int[][] board, int cost, State pre, int[][] goal, int i1, int j1) {
        this.board = board;
        this.cost = cost;
        this.pre = pre;
        this.goalStateBoard = goal;
        this.tag = "";
        this.i1 = i1;
        this.j1 = j1;
        this.i2 = -1;
        this.j2 = -1;
        stateID++;
    }

    /**
     * Constructor for a game with two empty panels.
     */
    public State(int[][] board, int cost, State pre, int[][] goal, int i1, int j1, int i2, int j2) {
        this.board = board;
        this.cost = cost;
        this.pre = pre;
        this.goalStateBoard = goal;
        this.tag = "";
        this.i1 = i1;
        this.j1 = j1;
        this.i2 = i2;
        this.j2 = j2;
        stateID++;
    }

    /**
     * This method return the board of the state.
     *
     * @return - The board.
     */
    public int[][] getBoard() {
        return this.board;
    }

    /**
     * This method return the cost of the state.
     *
     * @return - The cost.
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * This method return the pre of this state.
     *
     * @return - The pre.
     */
    public State getPre() {
        return this.pre;
    }

    /**
     * This method return string how we got to this state.
     *
     * @return - strPath.
     */
    public String getStrPath() {
        return this.strPath;
    }

    /**
     * This method return the tag string.
     *
     * @return - tag.
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * This method set the tag to be s.
     *
     * @param s - the new tag.
     */
    public void setTag(String s) {
        this.tag = s;
    }

    /**
     * This method return the ID of this State.
     *
     * @return - The ID.
     */
    public int getID() {
        return this.stateID;
    }

    /**
     * This method return the goal state of this State.
     *
     * @return - goal state.
     */
    public int[][] getGoal() {
        return this.goalStateBoard;
    }

    /**
     * This method return the index I that containing an empty panel on the state.
     *
     * @return - The I index.
     */
    public int getI1() {
        return this.i1;
    }

    /**
     * This method return the index J that containing an empty panel on the state.
     *
     * @return - The J index.
     */
    public int getJ1() {
        return this.j1;
    }


    /**
     * This method return the index I that containing an empty panel on the state(the second one).
     * (for board with two empty panel).
     *
     * @return - The I index.
     */
    public int getI2() {
        return this.i2;
    }

    /**
     * This method return the index J that containing an empty panel on the state(the second one).
     * (for board with two empty panel).
     *
     * @return - The J index.
     */
    public int getJ2() {
        return this.j2;
    }

    /**
     * This method makes a deep copy to the board.
     *
     * @return - New board.
     */
    private int[][] deepCopyArr(int[][] arr) {
        int row = arr.length;
        int col = arr[0].length;
        int[][] ans = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                ans[i][j] = arr[i][j];
            }
        }
        return ans;
    }

    /**
     * @param i1 - The first row that containing an empty panel.
     * @param j1 - The first col that containing an empty panel.
     * @param i2 - The second row that containing an empty panel.
     * @param j2 - The second col that containing an empty panel.
     * @return
     */
    private int ifClose(int i1, int j1, int i2, int j2) {
        if (i1 == -1 || i2 == -1 || j1 == -1 || j2 == -1) return 0;
        if (Math.abs(i1 - i2) == 1 && j1 == j2) return 1;
        if (Math.abs(j1 - j2) == 1 & i1 == i2) return -1;
        return 0;
    }


    /**
     * This method return a new state after moving one of the components from the board left.
     *
     * @param board - The existing board.
     * @param i     - The row that containing an empty panel.
     * @param j     - The col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveLeft(int[][] board, int i, int j, int i2, int j2) {
        if (j == this.board[0].length - 1) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i, j + 1, i2, j2);
        ans.board[i][j] = ans.board[i][j + 1];
        ans.board[i][j + 1] = 0;
        ans.strPath = ans.board[i][j] + "L";
        return ans;
    }

    /**
     * This method return a new state after moving one of the components from the board right.
     *
     * @param board - The existing board.
     * @param i     - The row that containing an empty panel.
     * @param j     - The col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveRight(int[][] board, int i, int j, int i2, int j2) {
        if (j == 0) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i, j - 1, i2, j2);
        ans.board[i][j] = ans.board[i][j - 1];
        ans.board[i][j - 1] = 0;
        ans.strPath = ans.board[i][j] + "R";
        return ans;
    }

    /**
     * This method return a new state after moving one of the components from the board up.
     *
     * @param board - The existing board.
     * @param i     - The row that containing an empty panel.
     * @param j     - The col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveUp(int[][] board, int i, int j, int i2, int j2) {
        if (i == board.length - 1) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i + 1, j, i2, j2);
        ans.board[i][j] = ans.board[i + 1][j];
        ans.board[i + 1][j] = 0;
        ans.strPath = ans.board[i][j] + "U";
        return ans;
    }

    /**
     * This method return a new state after moving one of the components from the board down.
     *
     * @param board - The existing board.
     * @param i     - The row that containing an empty panel.
     * @param j     - The col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveDown(int[][] board, int i, int j, int i2, int j2) {
        if (i == 0) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i - 1, j, i2, j2);
        ans.board[i][j] = ans.board[i - 1][j];
        ans.board[i - 1][j] = 0;
        ans.strPath = ans.board[i][j] + "D";
        return ans;
    }

    /**
     * This method return a new state after moving two close panels from the board up.
     *
     * @param board - The existing board.
     * @param i1    - The first row that containing an empty panel.
     * @param j1    - The first col that containing an empty panel.
     * @param i2    - The second row that containing an empty panel.
     * @param j2    - The second col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveLeftTwo(int[][] board, int i1, int j1, int i2, int j2) {
        if (j1 == board[0].length - 1) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i1, j1 + 1, i2, j2 + 1);
        ans.board[i1][j1] = ans.board[i1][j1 + 1];
        ans.board[i1][j1 + 1] = 0;
        ans.board[i2][j2] = ans.board[i2][j2 + 1];
        ans.board[i2][j2 + 1] = 0;
        ans.strPath = ans.board[i1][j1] + "&" + ans.board[i2][j2] + "L";
        return ans;
    }

    /**
     * This method return a new state after moving two close panels from the board up.
     *
     * @param board - The existing board.
     * @param i1    - The first row that containing an empty panel.
     * @param j1    - The first col that containing an empty panel.
     * @param i2    - The second row that containing an empty panel.
     * @param j2    - The second col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveRightTwo(int[][] board, int i1, int j1, int i2, int j2) {
        if (j1 == 0) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i1, j1 - 1, i2, j2 - 1);
        ans.board[i1][j1] = ans.board[i1][j1 - 1];
        ans.board[i1][j1 - 1] = 0;
        ans.board[i2][j2] = ans.board[i2][j2 - 1];
        ans.board[i2][j2 - 1] = 0;
        ans.strPath = ans.board[i1][j1] + "&" + ans.board[i2][j2] + "R";
        return ans;
    }

    /**
     * This method return a new state after moving two close panels from the board up.
     *
     * @param board - The existing board.
     * @param i1    - The first row that containing an empty panel.
     * @param j1    - The first col that containing an empty panel.
     * @param i2    - The second row that containing an empty panel.
     * @param j2    - The second col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveUpTwo(int[][] board, int i1, int j1, int i2, int j2) {
        if (i1 == board.length - 1) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i1 + 1, j1, i2 + 1, j2);
        ans.board[i1][j1] = ans.board[i1 + 1][j1];
        ans.board[i1 + 1][j1] = 0;
        ans.board[i2][j2] = ans.board[i2 + 1][j2];
        ans.board[i2 + 1][j2] = 0;
        ans.strPath = ans.board[i1][j1] + "&" + ans.board[i2][j2] + "U";
        return ans;
    }

    /**
     * This method return a new state after moving two close panels from the board down.
     *
     * @param board - The existing board.
     * @param i1    - The first row that containing an empty panel.
     * @param j1    - The first col that containing an empty panel.
     * @param i2    - The second row that containing an empty panel.
     * @param j2    - The second col that containing an empty panel.
     * @return - The new state, null if it failed.
     */
    private State moveDownTwo(int[][] board, int i1, int j1, int i2, int j2) {
        if (i1 == 0) {
            return null;
        }
        State ans = new State(deepCopyArr(board), this.cost + 5, this, this.goalStateBoard, i1 - 1, j1, i2 - 1, j2);
        ans.board[i1][j1] = ans.board[i1 - 1][j1];
        ans.board[i1 - 1][j1] = 0;
        ans.board[i2][j2] = ans.board[i2 - 1][j2];
        ans.board[i2 - 1][j2] = 0;
        ans.strPath = ans.board[i1][j1] + "&" + ans.board[i2][j2] + "D";
        return ans;
    }

    /**
     * This method returns all the new states that can be created by running all possible operators on the given board.
     * We will first check whether it is possible to move two squares vertically or horizontally and then we will
     * perform all the possible operators on the board.
     *
     * @param board - The given board.
     * @param i1    - The row that containing an empty panel.
     * @param j1    - The col that containing an empty panel.
     * @param i2    - The second row that containing an empty panel.
     * @param j2    - The second col that containing an empty panel.
     * @return
     */
    public ArrayList<State> performingOperators(int[][] board, int i1, int j1, int i2, int j2) {
        int whichOperator = ifClose(i1, j1, i2, j2);
        ArrayList<State> stateList = new ArrayList<>();
        if (whichOperator == 1) {
            State left = moveLeftTwo(board, i1, j1, i2, j2);
            State right = moveRightTwo(board, i1, j1, i2, j2);
            if (!stateList.contains(left) && left != null) {
                stateList.add(left);
            }
            if (!stateList.contains(right) && right != null) {
                stateList.add(right);
            }

        } else if (whichOperator == -1) {
            State up = moveUpTwo(board, i1, j1, i2, j2);
            State down = moveDownTwo(board, i1, j1, i2, j2);
            if (!stateList.contains(up) && up != null) {
                stateList.add(up);
            }
            if (!stateList.contains(down) && down != null) {
                stateList.add(down);
            }
        }

        State left = moveLeft(board, i1, j1, i2, j2);
        State right = moveRight(board, i1, j1, i2, j2);
        State up = moveUp(board, i1, j1, i2, j2);
        State down = moveDown(board, i1, j1, i2, j2);
        if (!stateList.contains(left) && left != null) {
            stateList.add(left);
        }
        if (!stateList.contains(up) && up != null) {
            stateList.add(up);
        }
        if (!stateList.contains(right) && right != null) {
            stateList.add(right);
        }
        if (!stateList.contains(down) && down != null) {
            stateList.add(down);
        }
        if (i2 != -1 && i1 != -1) {
            State left2 = moveLeft(board, i2, j2, i1, j1);
            State right2 = moveRight(board, i2, j2, i1, j1);
            State up2 = moveUp(board, i2, j2, i1, j1);
            State down2 = moveDown(board, i2, j2, i1, j1);
            if (!stateList.contains(left2) && left2 != null) {
                stateList.add(left2);
            }
            if (!stateList.contains(up2) && up2 != null) {
                stateList.add(up2);
            }
            if (!stateList.contains(right2) && right2 != null) {
                stateList.add(right2);
            }
            if (!stateList.contains(down2) && down2 != null) {
                stateList.add(down2);
            }
        }

        if (stateList.contains(this.pre)) {
            stateList.remove(this.pre);
        }
        return stateList;
    }


    /**
     * A heuristic function(Manhattan distance), which measures approximately the distance of each current state from the goal state.
     */
    public int heuristicFunc(int[][] goal) {
        int ans = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                int[] goalPlace = searchGoalNumberPlace(goal, this.board[i][j]);
                ans += (Math.abs(i - goalPlace[0]) + Math.abs(j - goalPlace[1]));
            }
        }
        return ans;
    }

    /**
     * This method find the place of the row and the col on the goal board
     *
     * @param goal - the goal board.
     * @param num  - the number we search in the goal board.
     * @return - an array -> arr[0] = row, arr[1] = col.
     */
    private int[] searchGoalNumberPlace(int[][] goal, int num) {
        int[] ans = new int[2];
        for (int i = 0; i < goal.length; i++) {
            for (int j = 0; j < goal[0].length; j++) {
                if (goal[i][j] == num) {
                    ans[0] = i;
                    ans[1] = j;
                    return ans;
                }
            }
        }
        return ans;
    }

    /**
     * This method print state.
     *
     * @return - String of state.
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < board.length; i++) {
            s += "\n";
            for (int j = 0; j < board[0].length; j++) {
                s += board[i][j] + "\t";
            }
        }
        return s;
    }

    /**
     * This method Override the equals method.
     *
     * @return - String of state.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != state.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * This method override the compareTo function, now its compare with Manhattan Distance + the cost of the state until now.
     *
     * @param o - the second state.
     * @return - 1 - if this is less from o, -1 if o less then this, 0 if they are equals(its not possible because the ID number).
     */
    @Override
    public int compareTo(State o) {
        int thisDistance = this.heuristicFunc(this.goalStateBoard);
        int oDistance = o.heuristicFunc(o.goalStateBoard);
        if (this.getCost() + thisDistance > o.getCost() + oDistance) return 1;
        else if (this.getCost() + thisDistance < o.getCost() + oDistance) return -1;
        else if (this.getID() > o.getID()) return 1;
        else if (this.getID() < o.getID()) return -1;
        return 0;
    }
}
