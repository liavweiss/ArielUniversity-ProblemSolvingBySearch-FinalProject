import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class DFID extends Algo{

    /**
     * constructor.
     *
     * @param initialState - The initial state of the game.
     * @param g - A vector containing all the target states that need to be reached (at least one of them).
     * @param withOpen - flag if print the open list or not.
     */
    protected DFID(State initialState, Vector<State> g, boolean withOpen) {
        super(initialState, g, withOpen);
    }

    /**
     * DFID first performs a DFS to depth one. Then starts over executing DFS to depth two.
     * Continue to run DFS to successively greater depth until a solution is found.
     * Do this using the method limitedDFS.
     * Time complexity: O(b^d).
     * Space complexity: O(db) -> O(d).
     * (Where b is branching factor and d is the maximum depth of search tree).
     *
     * @return - the ans of the algorithms.
     */
    @Override
    public String Algo() {
        String cutoff = "cutOff";
        for (int depth = 1; depth < Integer.MAX_VALUE; depth++) {
            Hashtable<String, State> openList = new Hashtable<>();
            String result = limitedDFS(initialState, goals, depth, openList);
            if (!result.equals(cutoff)) return result;
        }
        return "no path\n" + "Num: " + numOfState;
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
            return print(start);
        } else if (limit == 0) {
            return "cutOff";
        } else {
            String isCutOff = "false";
            openListPrint(openList, this.withOpen); // if withOpen == true it will print the open list in this level.
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
    }
}
