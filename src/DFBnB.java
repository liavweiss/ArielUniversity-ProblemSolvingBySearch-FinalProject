import java.util.*;

public class DFBnB extends Algo{

    /**
     * constructor.
     *
     * @param initialState - The initial state of the game.
     * @param g - A vector containing all the target states that need to be reached (at least one of them).
     * @param withOpen - flag if print the open list or not.
     */
    protected DFBnB(State initialState, Vector<State> g, boolean withOpen) {
        super(initialState, g, withOpen);
    }

    /**
     * This algorithm works like a simple limited DFS but when finding the first solution the cost of that solution is
     * stored in t(threshold) from this point on, each time the cost of the new path exceeds or equals t, that branch is pruned and we continue checking the
     * next one each time we reach a path that costs less than t we change t to this cost and update the best solution.
     * The search ends when we finish checking the whole tree.
     *
     * @return - the ans of the algorithms.
     */
    @Override
    public String Algo() {
        Stack<State> st = new Stack<>();
        Hashtable<String, State> openList = new Hashtable<>();
        st.push(initialState);
        String ans = "no path";
        openList.put(initialState.toString(), initialState);
        int infinity = Integer.MAX_VALUE;
        int t = infinity;
        while (!st.isEmpty()) {
            openListPrint(openList, this.withOpen); // if withOpen == true it will print the open list in this level.
            State s = st.pop();
            if (s.getTag().equals("out")) {
                openList.remove(s.toString());
            } else {
                s.setTag("out");
                st.push(s);
                ArrayList<State> arrState = s.performingOperators(s.getBoard(), s.getI1(), s.getJ1(), s.getI2(), s.getJ2());
                arrState.sort(State::compareTo);
                for (int i = 0; i < arrState.size(); i++) {
                    State operator = arrState.get(i);
                    numOfState++;
                    int funcOperator = operator.getCost() + operator.manhattanDistance(operator.getGoal());  // f(operator) = g(operator) + h(operator)
                    if (funcOperator >= t) {
                        arrState.subList(i, arrState.size()).clear();
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
                    } else if (goals.contains(operator)) {
                        t = funcOperator;
                        ans = print(operator);
                        arrState.clear();
                    }
                }
                Collections.reverse(arrState);
                for (State temp : arrState) {
                    st.push(temp);
                    openList.put(temp.toString(), temp);
                }
            }
        }
        if(ans.equals("no path")){
            return ans += "\n" + "Num: " + numOfState;
        }
        return ans;
    }
}
