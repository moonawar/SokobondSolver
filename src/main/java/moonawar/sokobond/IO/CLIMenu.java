package moonawar.sokobond.IO;

import java.awt.*;

import javax.swing.JOptionPane;

public class CLIMenu {
    public static void splashScreen() {
        System.out.println("Welcome to Sokobond Solver!\n\n");
    }

    public static Integer startMenu() {
        System.out.println("Please choose one of the following options:");
        System.out.println("1. Solve a sokobond level");
        System.out.println("2. Exit");

        System.out.print("Your choice: ");
        Integer choice = Integer.parseInt(System.console().readLine());

        if (choice != 1 && choice != 2) {
            System.out.println("\nInvalid choice, please try again!!\n");
            return startMenu();
        }

        return choice;
    }

    public static String selectFilePath() {
        System.out.println("Select the level file:");
        
        FileDialog fd = new FileDialog((Frame)null, "Select Sokobond Level File");

        fd.setMode(FileDialog.LOAD);
        fd.setVisible(true);

        if (fd.getFile() == null) {
            if (JOptionPane.showConfirmDialog(null, "Cancel Sokobond Solver?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else {
                return selectFilePath();
            }

            System.out.println("No file selected, please try again!!\n");
            return selectFilePath();
        }

        String filename = fd.getFile();
        String directory = fd.getDirectory();
        return directory + filename;
    }
}
