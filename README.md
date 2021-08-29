# ArielUniversity-ProblemSolvingBySearch-FinalProject

The final project of the problem-solving by search course 
Detailed in ex1_2021.pdf

## explanation of the project idea:

## Program structure:

### State:
This class represents a state,the state will be used by us to solve the puzzle tile-NxM game.
Each state will have several fields:
  
| **param**      |    **Details**        |
|-----------------|-----------------------|
| `board` | The game board in the given state. |
| `cost` |The price it cost us to get from the initial state to the new state. |
| `pre` | Which "state" he came from. |
| `strPath` | string ot the path from start to the goal state. |
| `counter` | to increase the statID every time. |
| `stateID` | Each situation will have its own ID  to use in  the priority queue, in a situation where two states have equality in their costs.|
| `goalStateBoard` | the target State, use for priority queue and Manhattan Distance heuristic function. |
| `strPre` | The action that brought  the state. |
| `tag` | for IDA* algorithm(it symbolizes whether the state is marked as "out" or not).|
| `i1` | The first row that containing an empty panel.|
| `j1` | The first col that containing an empty panel. |
| `i2` | The second row that containing an empty panel. |
| `j2` | The second col that containing an empty panel. |


### Algo:
This abstract class represents a frame for finding the way to solve the puzzle.(strategy design pattern) The class contain all the necessary thing for solving such as:initialstate,goal etc.. You need to extend this class and implements Algo() function.

| **param**      |    **Details**        |
|-----------------|-----------------------|
| `initialState` | The initial state of the game. |
| `goals` | A vector containing all the target states that need to be reached (at least one of them). |
| `withOpen` | flag if print the open list or not. |
| `numOfState` | counter states. |


### BFS:
This class extends Algo represents implementaion of BFS algorithm.

### DFID:
This class extends Algo represents implementaion of DFID algorithm.

### Astar:
This class extends Algo represents implementaion of A* algorithm .

### IDAstar:
This class extends Algo represents implementaion of IDA* algorithm.

### DFBnB:
This class extends Algo represents implementaion of DFBnB algorithm.

### Ex1:
This class represents the main of the NxM puzzle game.
In this game we will get a start state and a goal state and we will have to find the way to the goal mode.
We do this using 5 algorithms:
1.BFS.  2.A*  3.IDA*   4.DFID.   5.DFBnB.
In this class we get a txt file and from it we read our initial state and the end state and from them create an output
file which will be our solution ways of the game.
