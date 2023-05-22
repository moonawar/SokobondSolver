package moonawar.sokobond.GridSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import lombok.*;
import moonawar.sokobond.Chemistry.*;
import moonawar.sokobond.Exception.*;

public class Grid implements Cloneable, Serializable{
    @Getter private Integer rowCount; // number of rows in the grid
    @Getter private Integer colCount; // number of columns in the grid
    private Tile[][] buffer; // buffer of the grid value

    @Getter private Compound compound;
    @Getter private Vector2 compoundLocation; // must be of length 2
    @Getter private List<Element> freeElements; // elements that are not bonded to any other element
    private List<Vector2> freeElemLocations; // locations of free elements to speed up the process 

    private final Vector2 UP = new Vector2(0, -1);
    private final Vector2 DOWN = new Vector2(0, 1);
    private final Vector2 LEFT = new Vector2(-1, 0);
    private final Vector2 RIGHT = new Vector2(1, 0);

    public Grid(Integer r, Integer c, Tile[][] buffer, Compound compound, Vector2 compoundLocation, List<Element> freeElements) {
        this.rowCount = r;
        this.colCount = c;
        this.buffer = buffer;
        this.compound = compound;
        this.compoundLocation = compoundLocation;
        this.freeElements = freeElements;
        this.freeElemLocations = new ArrayList<Vector2>();
        
        for (Integer i = 0; i < this.rowCount; i++) {
            for (Integer j = 0; j < this.colCount; j++) {
                for (Element e : this.freeElements) {
                    if (this.buffer[i][j] == e) {
                        this.freeElemLocations.add(new Vector2(j, i));
                    }
                }
            }
        }
    }

    public Grid(Integer r, Integer c, Tile[][] buffer) {
        this(r, c, buffer, null, null, new ArrayList<Element>());
    }

    public Grid(Integer r, Integer c) {
        this(r, c, new Tile[r][c]);
    }

    @Override
    public Grid clone() {
        Grid deepCopy = (Grid) SerializationUtils.clone(this);
        return deepCopy;
    }

    public void setCompound(Compound compound, Vector2 compoundLocation) {
        this.compound = compound;
        this.compoundLocation = compoundLocation;
    }

    public void setFreeElements(List<Element> freeElmts) {
        this.freeElements = freeElmts;
        
        for (Integer i = 0; i < this.rowCount; i++) {
            for (Integer j = 0; j < this.colCount; j++) {
                for (Element e : freeElements) {
                    if (Element.class.isAssignableFrom(this.buffer[i][j].getClass())) {
                        if (this.buffer[i][j] == e) {
                            this.freeElemLocations.add(new Vector2(j, i));
                        }
                    }
                }
            }
        }
    }

    public Tile getTile(Integer r, Integer c) {
        if (r < 0 || r >= this.rowCount || c < 0 || c >= this.colCount) {
            throw new GridIndexOutOfBounds(r, c, this.rowCount, this.colCount);
        }
    
        return this.buffer[r][c];
    }

    public Tile getTile(Vector2 location) {
        return this.getTile(location.getY(), location.getX()); // in buffer, row is y, col is x
    }

    public void setTile(Integer r, Integer c, Tile t) {
        if (r < 0 || r >= this.rowCount || c < 0 || c >= this.colCount) {
            throw new GridIndexOutOfBounds(r, c, this.rowCount, this.colCount);
        }

        this.buffer[r][c] = t;
    }

    public void setTile(Vector2 location, Tile t) {
        this.setTile(location.getY(), location.getX(), t);
    }
    
    public void setTileVisited(Integer r, Integer c) {
        Tile t = this.getTile(r, c);
        t.setIsVisited(true);
        this.setTile(r, c, t);
    }

    public void setTileVisited(Vector2 location) {
        Tile t = this.getTile(location);
        t.setIsVisited(true);
        this.setTile(location, t);
    }

    public void setTileUnvisited(Integer r, Integer c) {
        Tile t = this.getTile(r, c);
        t.setIsVisited(false);
        this.setTile(r, c, t);
    }

    public void setTileUnvisited(Vector2 location) {
        Tile t = this.getTile(location);
        t.setIsVisited(false);
        this.setTile(location, t);
    }

    public void setAllTilesUnvisited() {
        for (Integer i = 0; i < this.rowCount; i++) {
            for (Integer j = 0; j < this.colCount; j++) {
                this.setTileUnvisited(i, j);
            }
        }
    }

    public void print() {
        for (Integer i = 0; i < this.rowCount; i++) {
            for (Integer j = 0; j < this.colCount; j++) {
                System.out.print(this.buffer[i][j].getValueChar());
            }
            System.out.println();
        }
    }

    public boolean canCompoundMoveTo(Character direction) {
        Vector2 shift = null;
        switch (direction) {
            case 'U':
                shift = UP;
                break;
            case 'D':
                shift = DOWN;
                break;
            case 'L':
                shift = LEFT;
                break;
            case 'R':
                shift = RIGHT;
                break;
            default:
                break;
        }

        if (shift == null) {
            throw new IllegalArgumentException("Direction must be one of U, D, L, R");
        }

        final Vector2 shiftFinal = shift;

        boolean canMove = this.compound.getBondedElements().stream()
            .allMatch(e -> {
                Vector2 newLocation = this.compoundLocation.add(e.getLocation()).add(shiftFinal);
                try {
                    Tile t = this.getTile(newLocation);

                    if (t.getType() == TileType.ELEMENT) {
                        Element pushedElmt = (Element) t;
                        if (!freeElements.contains(pushedElmt)) {
                            return !t.getIsVisited();
                        } else {
                            Vector2 pushedElmtLocation = newLocation.add(shiftFinal);
                            Tile pushedElmtTile;
                            try {
                                pushedElmtTile =  this.getTile(pushedElmtLocation);
                            } catch (GridIndexOutOfBounds ex) {
                                return false;
                            }
                            return pushedElmtTile.getType() != TileType.WALL && !pushedElmtTile.getIsVisited();
                        }
                    }
                    
                    return t.getType() != TileType.WALL && !t.getIsVisited();
                } catch (GridIndexOutOfBounds ex) {
                    return false;
                }
            });
        return canMove;
    }

    public void moveCompoundTo(Character direction) {
        Vector2 shift = null;
        switch (direction) {
            case 'U':
                shift = UP;
                break;
            case 'D':
                shift = DOWN;
                break;
            case 'L':
                shift = LEFT;
                break;
            case 'R':
                shift = RIGHT;
                break;
            default:
                break;
        }

        if (shift == null) {
            throw new IllegalArgumentException("Direction must be one of U, D, L, R");
        }

        Vector2 newLoc = this.compoundLocation.add(shift);
        
        setTile(this.compoundLocation, TileFactory.createEmptyTile());

        List<Element> processed = new ArrayList<>();
        
        for (Compound.BondedElement elmt : this.compound.getBondedElements()) {
            Vector2 elmtLocation = this.compoundLocation.add(elmt.getLocation());
            if (!processed.contains(getTile(elmtLocation))) {
                setTile(elmtLocation, TileFactory.createEmptyTile());
            }

            Vector2 newElmtLocation = newLoc.add(elmt.getLocation());
            if (freeElemLocations.stream().anyMatch(e -> e.equals(newElmtLocation))) {
                // System.out.println("Moving compound into free element");
                Vector2 pushedElmtLocation = newElmtLocation.add(shift);
                // System.out.println("Pushed element location: " + pushedElmtLocation.toString());
                setTile(pushedElmtLocation, getTile(newElmtLocation));
            }

            setTile(newElmtLocation, elmt.getElement());

            processed.add(elmt.getElement());
        }
        
        setTileVisited(this.compoundLocation);
        this.compoundLocation = newLoc;
        bondNearbyElements();
    }

    private void bondNearbyElements() {
        List<Compound.BondedElement> bondedElements = new ArrayList<>(this.compound.getBondedElements());
        for (Compound.BondedElement elmt : bondedElements) {
            Vector2 elmtLocation = this.compoundLocation.add(elmt.getLocation());
            Vector2[] nearbyLocations = new Vector2[] {
                elmtLocation.add(UP),
                elmtLocation.add(DOWN),
                elmtLocation.add(LEFT),
                elmtLocation.add(RIGHT)
            };

            for (Vector2 nearbyLocation : nearbyLocations) {
                try {
                    Tile t = this.getTile(nearbyLocation);
                    if (Element.class.isAssignableFrom(t.getClass())) {
                        Element nearbyElmt = (Element) t;
                        
                        if (!this.freeElements.contains(nearbyElmt)) {
                            continue;
                        }

                        setAllTilesUnvisited();
                        // bond the two elements
                        try {
                            elmt.getElement().bondElement(nearbyElmt);
                        } catch (BondLimitExceeded e) {
                            // ignore
                            continue;
                        }
                        this.compound.bondElement(nearbyElmt, nearbyLocation.subtract(this.compoundLocation));
                        
                        int idx = this.freeElements.indexOf(nearbyElmt);
                        this.freeElements.remove(nearbyElmt);
                        this.freeElemLocations.remove(idx);
                        
                    }
                } catch (GridIndexOutOfBounds ex) {
                    // ignore
                    continue;
                }
            }
        }   
    }

    public boolean isCompoundOnGoal() {
        return this.freeElements.size() == 0;
    }

    public Integer getHeuristic() {
        Integer heuristic = 0;
        heuristic += this.getClosestElmtDist();
    
        return heuristic;
    }

    public Integer getClosestElmtDist() {
        Integer closestDist = Integer.MAX_VALUE;
        for (Vector2 elmtLoc : this.freeElemLocations) {
            Integer dist = this.compoundLocation.manhattanDistance(elmtLoc);
            if (dist < closestDist) {
                closestDist = dist;
            }
        }
        return closestDist;
    }
}