package Screens.SystemScreens;

import Actions.Action;
import Models.DataModel;

import java.util.ArrayList;
import java.util.Scanner;

public class SystemViewConsole extends SystemView {
    // Constructor
    public SystemViewConsole(DataModel model) {
        super(model);
    }

    // General
    @Override
    public void title(String prompt) {
        System.out.println("\n" + prompt + "\n");
    }

    @Override
    public void message(String prompt) {
        System.out.println(prompt);
    }

    private String input(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Login methods
    @Override
    public void login() {
        username = this.input("Enter username: ");
        password = this.input("Enter password: ");
    }

    // Menu methods
    @Override
    public int menu(ArrayList<Action> actions) {
        System.out.println("Choose option:");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println(i + ". " + actions.get(i).name());
        }

        while (true) {
            try {
                int option = Integer.parseInt(this.input("Option: "));
                if (option < actions.size()) {
                    return option;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Option not found");
            }
        }
    }
}
