package moonawar.sokobond.GridSystem;

import java.io.Serializable;

import lombok.*;

@Getter @Setter @AllArgsConstructor @ToString
public class Vector2 implements Serializable{
    private Integer x;
    private Integer y;
    
    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public Integer manhattanDistance(Vector2 other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public boolean equals(Vector2 other) {
        return this.x.equals(other.x) && this.y.equals(other.y);
    }
}
