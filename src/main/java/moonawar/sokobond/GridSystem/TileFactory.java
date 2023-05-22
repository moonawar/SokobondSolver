package moonawar.sokobond.GridSystem;

import moonawar.sokobond.Chemistry.*;

public class TileFactory {
    public static Tile createEmptyTile() {
        return new Tile(' ', TileType.EMPTY);
    }

    public static Tile createWallTile() {
        return new Tile('#', TileType.WALL);
    }

    public static Tile createElementTile(Character elementName, Integer bondLimit) {
        return new Element(elementName, bondLimit); 
    }
}
