import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Vector;

public class AStar extends Algo{

    /**
     * constructor.
     *
     * @param initialState - The initial state of the game.
     * @param g - A vector containing all the target states that need to be reached (at least one of them).
     * @param withOpen - flag if print the open list or not.
     */
    protected AStar(State initialState, Vector<State> g, boolean withOpen) {
        super(initialState, g, withOpen);
    }

    /**
     * A* is an informed search algorithm, meaning that it is formulated in terms of weighted graphs:
     * starting from a specific starting state of a puzzle game, it aims to find a path to the given goal state having the smallest cost
     * It does this by priority queue at each iteration of its main loop, A* needs to determine which of its paths to extend.
     * At each iteration of its main loop, A* needs to determine which of its paths to extend.
     * It does so based on the cost of the path and an estimate of the cost required to extend the path all the way to the goal. Specifically,
     * A* selects the path that minimizes, do that by: f(n)=g(n)+h(n)
     * where n is the next node on the path, g(n) is the cost of the path from the start node to n, and h(n) is a heuristic function that estimates
     * the cost of the cheapest path from n to the goal.
     *
     * @return - the ans of the algorithms.
     */
    @Override
    public String Algo() {
        PriorityQueue<State> pq = new PriorityQueue<>();
        Hashtable<String, State> closedList = new Hashtable<>();
        Hashtable<String, State> openList = new Hashtable<>();
        pq.add(initialState);
        numOfState++;
        openList.put(initialState.toString(), initialState);
        while (!pq.isEmpty()) {
            openListPrint(openList, this.withOpen); // if withOpen == true it will print the open list in this level.
            State s = pq.remove();
            openList.remove(s.toString());
            if (goals.contains(s)) {
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
        return "no path\n" + "Num: " + numOfState;
    }
}
