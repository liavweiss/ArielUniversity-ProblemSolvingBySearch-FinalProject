import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Collections;

/**
 * This class represents solution algorithms for puzzle tile-NxM game.
 * The game solver algorithms are:
 * 1.BFS.
 * 2.A*
 * 3.IDA*
 * 4.DFID.
 * 5.DFBnB.
 *
 * @Author Liav Weiss.
 */
public class PuzzleGameAlgo {

    /**
     * This class has two fields:
     *
     * @param initialState - The initial state of the game.
     * @param goals - A vector containing all the target states that need to be reached (at least one of them).
     * @param withOpen - flag if print the open list or not.
     * @param numOfState - counter states.
     */
    private State initialState;
    private Vector<State> goals;
    private boolean withOpen;
    public static int numOfState = 0;

    /**
     * constructor.
     */
    public PuzzleGameAlgo(State initialState, Vector<State> g, boolean withOpen) {
        this.initialState = initialState;
        this.goals = g;
        this.withOpen = withOpen;
    }

    /**
     * This method return the InitialState of this puzzle game (state start).
     *
     * @return - InitialState.
     */
    public State getInitialState() {
        return this.initialState;
    }

    /**
     * This method return vector of goals state of this puzzle game.
     *
     * @return -  vector of goals.
     */
    public Vector<State> getGoals() {
        return this.goals;
    }


    /**
     * In this algorithm we will first initialize a queue and hash table.
     * open list - for check if its contain a certain state in O(1).
     * close list - for all the state we have finished developing.
     * We will insert the starting vertex into them,as long as the queue is not empty we will continue
     * to develop the sons of the state coming out of the queue, and we will put them in the queue.
     * Each time we remove a state from the queue we will check if it is the target vertex.
     * Time complexity: O(b^d).
     * Space complexity: O(b^d).
     * (Where b is branching factor and d is the solution depth).
     *
     * @param start - the state we start from him.
     * @param Goals - vector(list) of the goals state.
     * @return - the ans of the algorithms.
     */
    public String BFS(State start, Vector<State> Goals) {
        if (Goals.contains(start)) {
            return print(start);
        }
        Queue<State> q = new LinkedList<>();
        Hashtable<String, State> openList = new Hashtable<>();
        Hashtable<String, State> closedList = new Hashtable<>();
        q.add(start);
        openList.put(start.toString(), start);
        numOfState++;
        while (!q.isEmpty()) {
            openListPrint(openList, this.withOpen);
            State s = q.poll();
            closedList.put(s.toString(), s);
            openList.remove(s.toString());
            ArrayList<State> arrState = s.performingOperators(s.getBoard(), s.getI1(), s.getJ1(), s.getI2(), s.getJ2());
            while (!arrState.isEmpty()) {
                State operator = arrState.remove(0);
                numOfState++;
                if (!closedList.containsKey(operator.toString())) {
                    if (!openList.containsKey(operator.toString())) {
                        if (Goals.contains(operator)) {
                            return print(operator);
                        } else {
                            q.add(operator);
                            openList.put(operator.toString(), operator);
                        }
                    }
                }
            }
        }
        return "no path";
    }

    /**
     * DFID first performs a DFS to depth one. Then starts over executing DFS to depth two.
     * Continue to run DFS to successively greater depth until a solution is found.
     * Do this using the method limitedDFS.
     * Time complexity: O(b^d).
     * Space complexity: O(db) -> O(d).
     * (Where b is branching factor and d is the maximum depth of search tree).
     *
     * @param start - the state we start from him.
     * @param Goals - vector(list) of the goals state.
     * @return - the ans of the algorithms.
     */
    public String DFID(State start, Vector<State> Goals) {
        String cutoff = "cutOff";
        for (int depth = 1; depth < Integer.MAX_VALUE; depth++) {
            Hashtable<String, State> openList = new Hashtable<>();
            String result = limitedDFS(start, Goals, depth, openList);
            if (!result.equals(cutoff)) return "no path";
        }
        return "no path";
    }

    /**
     * Private method to find the state goal each time with different limit.
     *
     * @param start    - the state we start from him.
     * @param Goals    - vector(list) of the goals state.
     * @param limit    - the limit at which the algorithm will stop searching
     * @param openList - for check if its contain a certain state in O(1).
     * @return - string, "fail" if its fail or "cutOff" if its need to continue to the next limit.
     * if its find the goal state it will print it.
     */
    private String limitedDFS(State start, Vector<State> Goals, int limit, Hashtable<String, State> openList) {
        String cutOff = "cutOff";
        if (Goals.contains(start)) {
            print(start);
        } else if (limit == 0) {
            return "cutOff";
        } else {
            String isCutOff = "false";
            openList.put(start.toString(), start);
            ArrayList<State> arrState = start.performingOperators(start.getBoard(), start.getI1(), start.getJ1(), start.getI2(), start.getJ2());
            for (State s : arrState) {
                numOfState++;
                if (openList.containsKey(s.toString())) {
                    continue;
                }
                String result = limitedDFS(s, Goals, limit - 1, openList);
                if (result.equals(cutOff)) {
                    isCutOff = "true";
                } else if (!result.equals("fail")) {
                    return result;
                }
            }
            openList.remove(start.toString());
            if (isCutOff.equals("true")) {
                return cutOff;
            } else {
                return "fail";
            }
        }
        return "";
    }

    public String AStar(State start, Vector<State> Goals) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        Hashtable<String, State> closedList = new Hashtable<>();
        Hashtable<String, State> openList = new Hashtable<>();
        pq.add(start);
        numOfState++;
        openList.put(start.toString(), start);
        while (!pq.isEmpty()) {
            openListPrint(openList, this.withOpen);
            State s = pq.remove();
            openList.remove(s.toString());
            if (Goals.contains(s)) {
                return print(s);
            }
            closedList.put(s.toString(), s);
            ArrayList<State> arrState = s.performingOperators(s.getBoard(), s.getI1(), s.getJ1(), s.getI2(), s.getJ2());
            while (!arrState.isEmpty()) {
                State operator = arrState.remove(0);
                numOfState++;
                if (!closedList.containsKey(operator.toString()) && !openList.containsKey(operator)) {
                    pq.add(operator);
                    openList.put(operator.toString(), operator);
                } else if (openList.containsKey(operator) && openList.get(operator.toString()).compareTo(operator) == -1) {
                    State del = openList.remove(operator.toString());
                    pq.remove(del);
                    pq.add(operator);
                    openList.put(operator.toString(), operator);
                }
            }
        }
        return "no path";
    }

    public String IDAStar(State start, Vector<State> Goals) {
        Stack<State> st = new Stack<>();
        Hashtable<String, State> openList = new Hashtable<>();
        int t = start.manhattanDistance(start.getGoal());
        int infinity = Integer.MAX_VALUE;
        while (t != infinity) {
            int minF = infinity;
            st.add(start);
            openList.put(start.toString(), start);
            while (!st.isEmpty()) {
                openListPrint(openList, this.withOpen);
                State s = st.pop();
                if (s.getTag().equals("out")) {
                    openList.remove(s.toString());
                } else {
                    s.setTag("out");
                    st.push(s);
                    ArrayList<State> arrState = s.performingOperators(s.getBoard(), s.getI1(), s.getJ1(), s.getI2(), s.getJ2());
                    for (State operator : arrState) {
                        numOfState++;
                        int funcOperator = operator.getCost() + operator.manhattanDistance(operator.getGoal()); // f(operator) = g(operator) + h(operator)
                        if (funcOperator > t) {
                            minF = Math.min(minF, funcOperator);
                            continue;
                        }
                        State operatorTag = openList.get(operator.toString());
                        if (operatorTag != null && operatorTag.getTag().equals("out")) {
                            continue;
                        }
                        if (operatorTag != null && !operatorTag.getTag().equals("out")) {
                            int funcOperatorTag = operatorTag.getCost() + operatorTag.manhattanDistance(operatorTag.getGoal());  // f(operatorTag) = g(operatorTag) + h(operatorTag)
                            if (funcOperatorTag > funcOperator) {
                                openList.remove(operatorTag.toString());
                                st.remove(operatorTag);
                            } else {
                                continue;
                            }
                        }
                        if (Goals.contains(operator)) {
                            return print(operator);
                        }
                        openList.put(operator.toString(), operator);
                        st.push(operator);
                    }
                }
            }
            t = minF;
            start.setTag("");
        }
        return "no path";
    }

    public String DFBnB(State start, Vector<State> Goals) {
        Stack<State> st = new Stack<>();
        Hashtable<String, State> openList = new Hashtable<>();
        st.push(start);
        openList.put(start.toString(), start);
        int infinity = Integer.MAX_VALUE;
        int t = infinity;
        while (!st.isEmpty()) {
            openListPrint(openList, this.withOpen);
            State s = st.pop();
            if (s.getTag().equals("out")) {
                openList.remove(s.toString());
            } else {
                s.setTag("out");
                st.push(s);
                ArrayList<State> arrState = s.performingOperators(s.getBoard(), s.getI1(), s.getJ1(), s.getI2(), s.getJ2());
                int counter = 0;
                int n = 0;
                arrState.sort(State::compareTo);
                while (n < arrState.size()) {
                    State operator = arrState.get(n++);
                    numOfState++;
                    int funcOperator = operator.getCost() + operator.manhattanDistance(operator.getGoal());  // f(operator) = g(operator) + h(operator)
                    if (funcOperator >= t) {
                        while (counter < arrState.size()) {
                            arrState.remove(counter++);
                            n++;
                        }
                    } else if (openList.containsKey(operator.toString()) && openList.get(operator.toString()).equals("out")) {
                        arrState.remove(operator);
                    } else if (openList.containsKey(operator.toString()) && !openList.get(operator.toString()).equals("out")) {
                        int funcOperatorTag = openList.get(operator.toString()).manhattanDistance(openList.get(operator.toString()).getGoal());  // f(funcOperatorTag) = g(funcOperatorTag) + h(funcOperatorTag)
                        if (funcOperator >= funcOperatorTag) {
                            arrState.remove(operator);
                        } else {
                            st.remove(openList.get(operator.toString()));
                            openList.remove(operator.toString());
                        }
                    } else if (Goals.contains(operator)) {
                        return print(operator);
                    }
                    counter++;
                }
                Collections.reverse(arrState);
                for (State temp : arrState) {
                    st.push(temp);
                    openList.put(temp.toString(), temp);
                }
            }
        }
        return "no path";
    }


    /**
     * This method returns a list of states up to the target state.
     *
     * @param g - The last state on the list.
     * @return - list of the states until the goal state.
     */
    private List<State> goalList(State g) {
        List<State> finalList = new LinkedList<>();
        while (g != null) {
            finalList.add(g);
            g = g.getPre();
        }
        return finalList;
    }


    private String print(State g) {
        List<State> tempList = goalList(g);
        if (tempList.isEmpty()) {
            System.out.println("");
        }
        String ans = tempList.remove(0).getStrPath();
        while (!tempList.isEmpty()) {
            State s = tempList.remove(0);
            if (s.getStrPath() != null) {
                ans = s.getStrPath() + "-" + ans;
            }
        }
        ans+="\n";
        ans+="Num: " + numOfState + "\n";
        ans+="Cost: " + g.getCost() + "\n";
        return ans;
    }

    /**
     * This function call the specific algorithm from the input.
     * (its use for the main of the program).
     *
     * @param algorithm - the algorithm we will use.
     * @return - the ans of the algorithms.
     */
    public String collAlgorithm(String algorithm) {
        String ans = "";
        switch (algorithm) {
            case "BFS":
                ans =this.BFS(this.getInitialState(), this.getGoals());
                break;
            case "DFID":
                ans =this.DFID(this.getInitialState(), this.getGoals());
                break;
            case "A*":
                ans = this.AStar(this.getInitialState(), this.getGoals());
                break;
            case "IDA*":
                ans =this.IDAStar(this.getInitialState(), this.getGoals());
                break;
            case "DFBnB":
                ans = this.DFBnB(this.getInitialState(), this.getGoals());
                break;
        }
        return ans;
    }

    public void openListPrint(Hashtable<String, State> openList, boolean withOpen){
        if(withOpen == true){
            for(String key : openList.keySet()){
                System.out.println(key);
            }
        }
    }


    public static void main(String[] args) {
//        //input1
//        int[][] arr = {{1, 2, 3, 4}, {5, 6, 11, 7}, {9, 10, 8, 0}};
//        int[][] arr2 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 0}};
//        State start = new State(arr, 0, null, arr2, 2, 3);
//        State end = new State(arr2, 0, null, arr2, 2, 3);


        //input2.
        int[][] arr = {{1, 0, 4}, {3, 5, 6}, {2, 0, 7}};
        int[][] arr2 = {{1, 2, 3}, {4, 5, 6}, {7, 0, 0}};
        State start = new State(arr, 0, null, arr2, 0, 1, 2, 1);
        State end = new State(arr2, 0, null, arr2, 2, 1, 2, 2);

//        //my input
//        int[][] arr = {{2,3},
//                       {1,0}};
//        int[][] arr2 = {{1,2},{3,0}};
//        State start = new State(arr, 0, null,arr2, 1, 1,-1,-1);
//        State end = new State(arr2, 0, null,arr2, 1, 1,-1,-1);


        Vector<State> vec = new Vector<>();
        vec.add(end);
        File file = new File("");
        PuzzleGameAlgo puzzle = new PuzzleGameAlgo(start, vec, false);
        double startTime = System.currentTimeMillis();
        System.out.println(puzzle.IDAStar(puzzle.initialState, puzzle.goals));
        double stopTime = System.currentTimeMillis();
        System.out.println((stopTime - startTime) / 1000 + " second");


    }
}
