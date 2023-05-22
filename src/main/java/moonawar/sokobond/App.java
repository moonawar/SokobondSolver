package moonawar.sokobond;

import java.util.List;

import javax.swing.JOptionPane;

import moonawar.sokobond.Exception.GridFormatException;
import moonawar.sokobond.GridSystem.Grid;
import moonawar.sokobond.IO.CLIMenu;
import moonawar.sokobond.IO.GridReader;

/**
 * Hello world!
 */
public final class App {
    public static void main(String[] args) {
        /**
         * PSEUDOCODE:
         * Select file from input dialog
         * Read the file, try catch
         * Solve the sokobond problem with backtracking?/bnb?
        */
        CLIMenu.splashScreen();
        Integer choice = CLIMenu.startMenu();
        System.out.println();

        String filePath = "";
        if (choice == 1) {
            filePath = CLIMenu.selectFilePath();
            System.out.println(filePath);
        } else {
            System.exit(0);
        }

        Grid level = null;

        try {
            level = GridReader.read(filePath);
        } catch (GridFormatException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, e.getMessage(), "Invalid Format", JOptionPane.ERROR_MESSAGE);
            if (JOptionPane.showConfirmDialog(null, "Restart Sokobond Solver?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.out.println();
                main(args);
            } else {
                System.exit(0);
            }
            System.exit(0);
        }

        Solver solver = new Solver();
        // List<Character> result = null;
        try {
            long start = System.currentTimeMillis();
            List<Character> result = solver.solveSokobond(level);
            // result = solver.solveSokobond(level);
            long end = System.currentTimeMillis();
            System.out.println(result.toString());
            System.out.println("Time elapsed: " + (end - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(0);
        }
        // level.print();

        // Character solution[] = {'U', 'U', 'R', 'R', 'U', 'U', 'R', 'R'};
        // for (Character c : result) {
        //     System.out.println("====================================");
        //     System.out.println("Move " + c);
        //     level.moveCompoundTo(c);
        //     level.print();
        //     System.out.println("free elmt: " + level.getFreeElements().size());
        //     System.out.println("====================================");
        // }
        // level.print();

        // System.out.println("free elmt: " + level.getFreeElements().size());
        // System.out.println("Move to up");
        // level.moveCompoundTo('U');
        // level.print();
        // System.out.println("free elmt: " + level.getFreeElements().size());
        // System.out.println("Move to down");
        // level.moveCompoundTo('D');
        // level.print();
        // System.out.println("free elmt: " + level.getFreeElements().size());
        // System.out.println("Move to left");
        // level.moveCompoundTo('L');
        // level.print();
        // System.out.println("free elmt: " + level.getFreeElements().size());


        System.exit(0);
    }
}
