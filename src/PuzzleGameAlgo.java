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
     * @param numOfState - counter states.
     */
    private State initialState;
    private Vector<State> goals;
    public static int numOfState = 0;

    /**
     * constructor.
     */
    public PuzzleGameAlgo(State initialState, Vector<State> g) {
        this.initialState = initialState;
        this.goals = g;
    }

    public void BFS(State start, Vector<State> Goals) {
        if (Goals.contains(start)) {
            print(start);
            return;
        }
        Queue<State> q = new LinkedList<>();
        Hashtable<String, State> openList = new Hashtable<>();
        Hashtable<String, State> closedList = new Hashtable<>();
        q.add(start);
        openList.put(start.toString(), start);
        numOfState++;
        while (!q.isEmpty()) {
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
                            print(operator);
                            return;
                        } else {
                            q.add(operator);
                            openList.put(operator.toString(), operator);
                        }
                    }
                }
            }
        }
    }

    public void DFID(State start, Vector<State> Goals) {
        String cutoff = "cutOff";
        for (int depth = 1; depth < Integer.MAX_VALUE; depth++) {
            Hashtable<String, State> openList = new Hashtable<>();
            String result = limited_DFS(start, Goals, depth, openList);
            if (!result.equals(cutoff)) return;
        }
    }

    private String limited_DFS(State start, Vector<State> goal, int limit, Hashtable<String, State> openList) {
        String cutOff = "cutOff";
        if (goal.contains(start)) {
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
                String result = limited_DFS(s, goal, limit - 1, openList);
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

    private void AStar(State start, Vector<State> Goals) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        Hashtable<String, State> closedList = new Hashtable<>();
        Hashtable<String, State> openList = new Hashtable<>();
        pq.add(start);
        numOfState++;
        openList.put(start.toString(), start);
        while (!pq.isEmpty()) {
            State s = pq.remove();
            openList.remove(s.toString());
            if (Goals.contains(s)) {
                print(s);
                return;
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
    }

    private void IDAStar(State start, Vector<State> Goals) {
        Stack<State> st = new Stack<>();
        Hashtable<String, State> openList = new Hashtable<>();
        int t = start.heuristicFunc(start.getGoal());
        int infinity = Integer.MAX_VALUE;
        while (t != infinity) {
            int minF = infinity;
            st.add(start);
            openList.put(start.toString(), start);
            while (!st.isEmpty()) {
                State s = st.pop();
                if (s.getTag().equals("out")) {
                    openList.remove(s);
                } else {
                    s.setTag("out");
                    st.push(s);
                    ArrayList<State> arrState = s.performingOperators(s.getBoard(), s.getI1(), s.getJ1(), s.getI2(), s.getJ2());
                    for (State operator : arrState) {
                        numOfState++;
                        int funcOperator = operator.getCost() + operator.heuristicFunc(operator.getGoal()); // f(operator) = g(operator) + h(operator)
                        if (funcOperator > t) {
                            minF = Math.min(minF, funcOperator);
                            continue;
                        }
                        State operatorTag = openList.get(operator.toString());
                        if (operatorTag != null && operatorTag.getTag().equals("out")) {
                            continue;
                        }
                        if (operatorTag != null && !operatorTag.getTag().equals("out")) {
                            int funcOperatorTag = operatorTag.getCost() + operatorTag.heuristicFunc(operatorTag.getGoal());  // f(operatorTag) = g(operatorTag) + h(operatorTag)
                            if (funcOperatorTag > funcOperator) {
                                openList.remove(operatorTag.toString());
                                st.remove(operatorTag);
                            } else {
                                continue;
                            }
                        }
                        if (Goals.contains(operator)) {
                            print(operator);
                            return;
                        }
                        openList.put(operator.toString(), operator);
                        st.push(operator);
                    }
                }
            }
            t = minF;
            start.setTag("out");
        }

    }

    private void DFBnB(State start, Vector<State> Goals) {
        Stack<State> st = new Stack<>();
        Hashtable<String, State> openList = new Hashtable<>();
        st.push(start);
        openList.put(start.toString(), start);
        int infinity = Integer.MAX_VALUE;
        int t = infinity;
        numOfState++;
        while (!st.isEmpty()) {
            State s = st.pop();
            if (s.getTag().equals("out")) {
                openList.remove(s);
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
                    int funcOperator = operator.getCost() + operator.heuristicFunc(operator.getGoal());  // f(operator) = g(operator) + h(operator)
                    if (funcOperator >= t) {
                        while (counter < arrState.size()) {
                            arrState.remove(counter++);
                            n++;
                        }
                    } else if (openList.containsKey(operator.toString()) && openList.get(operator.toString()).equals("out")) {
                        arrState.remove(operator);
                    } else if (openList.containsKey(operator.toString()) && !openList.get(operator.toString()).equals("out")) {
                        int funcOperatorTag = openList.get(operator.toString()).heuristicFunc(openList.get(operator.toString()).getGoal());  // f(funcOperatorTag) = g(funcOperatorTag) + h(funcOperatorTag)
                        if (funcOperator >= funcOperatorTag) {
                            arrState.remove(operator);
                        } else {
                            st.remove(openList.get(operator.toString()));
                            openList.remove(operator.toString());
                        }
                    } else if (Goals.contains(operator)) {
                        print(operator);
                        return;
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


    private void print(State g) {
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
        System.out.println(ans);
        System.out.println("Num: " + numOfState);
        System.out.println("Cost: " + g.getCost());
    }

    public static void main(String[] args) {
//        //input1
        int[][] arr = {{1, 2, 3, 4}, {5, 6, 11, 7}, {9, 10, 8, 0}};
        int[][] arr2 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 0}};
        State start = new State(arr, 0, null, arr2, 2, 3);
        State end = new State(arr2, 0, null, arr2, 2, 3);


//        //input2.
//        int[][] arr = {{1,0,4}, {3,5,6}, {2,0,7}};
//        int[][] arr2 = {{1,2,3},{4,5,6},{7,0,0}};
//        State start = new State(arr, 0, null,arr2, 0, 1, 2,1);
//        State end = new State(arr2, 0, null,arr2, 2, 1,2,2);

//        //my input
//        int[][] arr = {{2,3},
//                       {1,0}};
//        int[][] arr2 = {{1,2},{3,0}};
//        State start = new State(arr, 0, null,arr2, 1, 1,-1,-1);
//        State end = new State(arr2, 0, null,arr2, 1, 1,-1,-1);


        Vector<State> vec = new Vector<>();
        vec.add(end);
        PuzzleGameAlgo puzzle = new PuzzleGameAlgo(start, vec);
        double startTime = System.nanoTime();
        puzzle.IDAStar(puzzle.initialState, puzzle.goals);
        double stopTime = System.nanoTime();
        System.out.println((stopTime - startTime) / 1000000000 + " second");


    }
}
