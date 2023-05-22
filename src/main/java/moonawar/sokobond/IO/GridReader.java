package moonawar.sokobond.IO;

import java.io.File;
import java.util.*;

import moonawar.sokobond.GridSystem.*;
import moonawar.sokobond.Chemistry.*;
import moonawar.sokobond.Exception.*;

public class GridReader {
    public static Grid read(String filePath) throws GridFormatException {
        File file = new File(filePath);
        
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String line;
        String[] lineSplit;

        line = scanner.nextLine();
        lineSplit = line.split(" ");

        Integer rows = Integer.parseInt(lineSplit[0]);
        Integer columns = Integer.parseInt(lineSplit[1]);
        Integer nElmt = Integer.parseInt(lineSplit[2]);

        List<Element> elements = new ArrayList<Element>();

        for (Integer i = 0; i < nElmt; i++) {
            line = scanner.nextLine();
            lineSplit = line.split(" ");

            Character name = lineSplit[0].charAt(0);
            Integer bondLimit = Integer.parseInt(lineSplit[1]);

            elements.add(new Element(name, bondLimit));
        }

        Grid grid = new Grid(rows, columns);
        List<Element> frElements = new ArrayList<>();

        for (Integer i = 0; i < rows; i++) {
            line = scanner.nextLine();
            for (Integer j = 0; j < columns; j++) {
                Character c = ' ';
                if (j < line.length()) {
                    c = line.charAt(j);
                }

                switch (c) {
                    case ' ':
                        try {
                            grid.setTile(i, j, TileFactory.createEmptyTile());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            scanner.close();
                            throw new GridFormatException("Grid format is not valid, index out of bounds");
                        }
                        break;
                    case '#':
                        try {
                            grid.setTile(i, j, TileFactory.createWallTile());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            scanner.close();
                            throw new GridFormatException("Grid format is not valid, index out of bounds");
                        }
                        break;
                    default:
                        Element elmt = null;
                        final Character cFinal = c;
                        if (elements.stream().anyMatch(e -> e.getName().equals(cFinal))) {
                            Element refs = elements.stream().filter(e -> e.getName().equals(cFinal)).findFirst().get();
                            elmt = new Element(refs.getName(), refs.getBondLimit());
                        }

                        if (elmt != null) {
                            try {
                                frElements.add(elmt);
                                grid.setTile(i, j, elmt);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                scanner.close();
                                throw new GridFormatException("Grid format is not valid, index out of bounds");
                            }
                        } else {
                            scanner.close();
                            throw new GridFormatException("Element " + c + " is not defined in the grid format");
                        }
                        break;
                }
            }
        }

        
        line = scanner.nextLine();
        lineSplit = line.split(" ");

        Integer rowLoc = Integer.parseInt(lineSplit[0]);
        Integer colLoc = Integer.parseInt(lineSplit[1]);
        
        Tile tile;
        try {
            tile = grid.getTile(rowLoc, colLoc);
            if (tile.getType() != TileType.ELEMENT) {
                scanner.close();
                throw new GridFormatException("Starting location is not an element");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            scanner.close();
            throw new GridFormatException("Starting location is out of bounds");
        }

        Element start = (Element) tile;
        grid.setFreeElements(frElements); // assumption is no element bonded to the starting element on the grid
        grid.getFreeElements().remove(start);
        grid.setCompound(new Compound(start), new Vector2(colLoc, rowLoc));
        
        scanner.close();
        return grid;
    }   
}
