import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

/**
 * this algorithm perform at each iteration a depth-first search, cutting off a branch when its total cost f(n)=g(n)+h(n) exceeds a given threshold.
 * This threshold starts at the estimate of the cost at the initial state, and increases for each iteration of the algorithm.
 * At each iteration, the threshold used for the next iteration is the minimum cost of all values that exceeded the current threshold.
 */
public class IDAStar extends Algo{

    /**
     * constructor.
     *
     * @param initialState - The initial state of the game.
     * @param g - A vector containing all the target states that need to be reached (at least one of them).
     * @param withOpen - flag if print the open list or not.
     */
    protected IDAStar(State initialState, Vector<State> g, boolean withOpen) {
        super(initialState, g, withOpen);
    }

    @Override
    public String Algo() {
        Stack<State> st = new Stack<>();
        Hashtable<String, State> openList = new Hashtable<>();
        int t = initialState.manhattanDistance(initialState.getGoal());
        int infinity = Integer.MAX_VALUE;
        while (t != infinity) {
            int minF = infinity;
            st.add(initialState);
            openList.put(initialState.toString(), initialState);
            while (!st.isEmpty()) {
                openListPrint(openList, this.withOpen); // if withOpen == true it will print the open list in this level.
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
                        if (goals.contains(operator)) {
                            return print(operator);
                        }
                        openList.put(operator.toString(), operator);
                        st.push(operator);
                    }
                }
            }
            t = minF;
            initialState.setTag("");
        }
        return "no path\n" + "Num: " + numOfState;
    }
}
