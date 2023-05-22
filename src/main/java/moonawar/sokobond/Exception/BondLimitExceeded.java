package moonawar.sokobond.Exception;

public class BondLimitExceeded extends RuntimeException{
    public BondLimitExceeded(Character element1, Integer bondLimit1, Character element2, Integer bondLimit2) {
        super("Bond limit exceeded between " + element1 + " and " + element2 + "\n" + element1 + " bond limit: " + bondLimit1 + "\n" + element2 + " bond limit: " + bondLimit2);
    }
}
