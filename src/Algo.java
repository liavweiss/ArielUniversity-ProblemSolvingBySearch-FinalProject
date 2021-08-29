import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * This abstract class represents a frame for finding the way to solve the puzzle.(strategy design pattern)
 * The class contain all the necessary thing for solving such as:initialState,goal etc..
 * You need to extend this class and implements Algo() method.
 */
public abstract class Algo {
    /**
     * This class has four fields:
     *
     * @param initialState - The initial state of the game.
     * @param goals - A vector containing all the target states that need to be reached (at least one of them).
     * @param withOpen - flag if print the open list or not.
     * @param numOfState - counter states.
     */
    protected State initialState;
    protected Vector<State> goals;
    protected boolean withOpen;
    public static int numOfState = 0;

    /**
     * constructor.
     */
    protected Algo(State initialState, Vector<State> g, boolean withOpen) {
        this.initialState = initialState;
        this.goals = g;
        this.withOpen = withOpen;
    }

    /**
     * This method returns the path from state start to state goal.
     *
     * @param g - The last state on the path.
     * @return - path of the states until the goal state.
     */
    protected String print(State g) {
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
        ans += "\n";
        ans += "Num: " + numOfState + "\n";
        ans += "Cost: " + g.getCost() + "\n";
        return ans;
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

    /**
     * This method print the open list.
     *
     * @param openList - the open list with the states.
     * @param withOpen - flag: if true it will print, if false it will not.
     */
    protected void openListPrint(Hashtable<String, State> openList, boolean withOpen) {
        if (withOpen == true) {
            System.out.println("start");
            for (String key : openList.keySet()) {
                System.out.println(key);
            }
            System.out.println("end");
        }
    }

    /**
     * The abstract method, will be override, for any algorithms you want to add.
     */
    public abstract String Algo();
}
