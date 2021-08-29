import java.util.*;

public class BFS extends Algo {

    /**
     * constructor.
     *
     * @param initialState - The initial state of the game.
     * @param g - A vector containing all the target states that need to be reached (at least one of them).
     * @param withOpen - flag if print the open list or not.
     */
    public BFS(State initialState, Vector<State> g, boolean withOpen) {
        super(initialState, g, withOpen);
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
     * @return - the ans of the algorithms.
     */
    @Override
    public String Algo() {
        if (goals.contains(initialState)) {
            return print(getInitialState());
        }
        Queue<State> q = new LinkedList<>();
        Hashtable<String, State> openList = new Hashtable<>();
        Hashtable<String, State> closedList = new Hashtable<>();
        q.add(getInitialState());
        openList.put(getInitialState().toString(), getInitialState());
        numOfState++;
        while (!q.isEmpty()) {
            openListPrint(openList, super.withOpen); // if withOpen == true it will print the open list in this level.
            State s = q.poll();
            closedList.put(s.toString(), s);
            openList.remove(s.toString());
            ArrayList<State> arrState = s.performingOperators(s.getBoard(), s.getI1(), s.getJ1(), s.getI2(), s.getJ2());
            while (!arrState.isEmpty()) {
                State operator = arrState.remove(0);
                numOfState++;
                if (!closedList.containsKey(operator.toString())) {
                    if (!openList.containsKey(operator.toString())) {
                        if (getGoals().contains(operator)) {
                            return super.print(operator);
                        } else {
                            q.add(operator);
                            openList.put(operator.toString(), operator);
                        }
                    }
                }
            }
        }
        return "no path\n" + "Num: " + numOfState;
    }
}
