package moonawar.sokobond.GridSystem;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tile implements Serializable {
    private Character valueChar;
    private TileType type;
    private Boolean isVisited;

    public Tile(Character valueChar, TileType type) {
        this.valueChar = valueChar;
        this.type = type;
        this.isVisited = false;
    }
}
