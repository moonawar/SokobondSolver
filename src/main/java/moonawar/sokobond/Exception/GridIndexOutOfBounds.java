package moonawar.sokobond.Exception;

public class GridIndexOutOfBounds extends RuntimeException {
    public GridIndexOutOfBounds(Integer x, Integer y, Integer width, Integer height) {
        super("Grid is out of bounds at (" + x + ", " + y + ") \nallowed index is [" + 0 + ", " + width + ") x [" + 0 + ", " + height + ")");
    }
}
