package moonawar.sokobond.Chemistry;

import java.util.ArrayList;

import lombok.*;
import moonawar.sokobond.Exception.BondLimitExceeded;
import moonawar.sokobond.GridSystem.*;

@Getter @Setter
public class Element extends Tile implements Cloneable{
    private final Character name;
    private final Integer bondLimit;
    private Integer bondCount;
    private ArrayList<Element> bondedElements;

    public Element(Character name, Integer bondLimit) {
        super(name, TileType.ELEMENT);
        this.name = name;
        this.bondLimit = bondLimit;
        this.bondCount = 0;
        this.bondedElements = new ArrayList<Element>();
    }

    public void bondElement(Element element) {
        if (this.bondCount >= this.bondLimit || element.bondCount >= element.bondLimit) {
            throw new BondLimitExceeded(this.name, this.bondLimit, element.name, element.bondLimit);
        }

        this.bondedElements.add(element);
        this.bondCount++;

        element.bondedElements.add(this);
        element.bondCount++;
    }

    @Override
    public Element clone() {
        Element clone = null;
        try {
            clone = (Element) super.clone();
            clone.bondedElements = new ArrayList<Element>();
            for (Element bondedElement : this.bondedElements) {
                clone.bondedElements.add(bondedElement);
            }
            clone.bondCount = Integer.valueOf(this.bondCount);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
