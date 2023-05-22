package moonawar.sokobond.Chemistry;

import java.io.Serializable;
import java.util.*;
import lombok.*;
import moonawar.sokobond.GridSystem.Vector2;


@Getter @Setter
public class Compound implements Serializable{
    private List<BondedElement> bondedElements;
    
    @Getter @Setter @AllArgsConstructor @ToString
    public class BondedElement implements Serializable {
        private Element element;
        private Vector2 location; // relative to the starting element, starting element is (0, 0)
    }

    private BondedElement startingElement;

    public Compound(Element startingElement) {
        this.startingElement = new BondedElement(startingElement, new Vector2(0, 0));
        this.bondedElements = new ArrayList<BondedElement>();
        this.bondedElements.add(this.startingElement);
    }

    public void bondElement(Element element, Vector2 location) { // location is relative to the starting element
        this.bondedElements.add(new BondedElement(element, location));
    }
}
