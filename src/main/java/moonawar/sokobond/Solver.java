package moonawar.sokobond;

import java.util.*;

import lombok.Getter;
import moonawar.sokobond.GridSystem.Grid;

public class Solver {
    private PriorityQueue<GridInfo> queue;
    @Getter private int counter;

    public Solver() {
        queue = new PriorityQueue<GridInfo>(new Comparator<GridInfo>() {
            @Override
            public int compare(GridInfo grid1, GridInfo grid2) {
                // System.out.println("comparing " + grid1.getDirections().toString() + " and " + grid2.getDirections().toString());
                // System.out.println("comparing " + grid1.getHeuristic() + " and " + grid2.getHeuristic());
                return (grid1.getHeuristic() + grid1.getCost()) - (grid2.getHeuristic() + grid2.getCost());
            }
        });
        counter = 1;
    }

    public List<Character> solveSokobond(Grid grid) {
        /** Solve the sokobond puzzle */
        List<Character> solution = new ArrayList<Character>();
        GridInfo start = new GridInfo(grid, grid.getHeuristic(), solution);
        generateChildren(start);
        System.out.println("Searching for solutions...");
        return AStar(queue.poll());
    }

    private List<Character> AStar(GridInfo gridInfo) {
        if (gridInfo.getDirections().toString().startsWith("[U, D, L, L, U, D")) {
            System.out.println(gridInfo.getDirections().toString());
            // System.out.println("Free elmt: " + gridInfo.getGrid().getFreeElements().size());
            gridInfo.getGrid().print();
        }

        if (gridInfo.getGrid().isCompoundOnGoal()) return gridInfo.getDirections();
        else {
            if (queue.isEmpty()) {
                generateChildren(gridInfo);
                if (queue.isEmpty()) throw new RuntimeException("No solution found");;
            } else {
                generateChildren(gridInfo);
            }
            
            return AStar(queue.poll());
        }
    }

    private void generateChildren(GridInfo gridInfo) {
        // printPrio();
        Character directions[] = {'R', 'L', 'D', 'U'};
        for (Character direction : directions) {
            if (!gridInfo.getGrid().canCompoundMoveTo(direction)) continue;

            Grid child = gridInfo.getGrid().clone();
            child.moveCompoundTo(direction);

            List<Character> directionsList = new ArrayList<Character>(gridInfo.getDirections());
            directionsList.add(direction);

            GridInfo childInfo = new GridInfo(child, child.getHeuristic(), directionsList);
            counter++;
            queue.add(childInfo);
        }
    }
}

@Getter
class GridInfo {
    private final Grid grid;
    private final Integer heuristic;
    private final List<Character> directions;

    public GridInfo(Grid grid, Integer heuristic, List<Character> directions) {
        this.grid = grid;
        this.heuristic = heuristic;
        this.directions = directions;
    }

    public Integer getCost() {
        return this.directions.size();
    }
}