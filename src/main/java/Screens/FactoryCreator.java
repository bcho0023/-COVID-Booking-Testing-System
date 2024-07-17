package Screens;

public class FactoryCreator {
    // Get factory matching screen
    public static AbstractFactory getFactory(String choice) {
        if (choice.equals("CONSOLE")) {
            return new ConsoleFactory();
        } else {
            return null;
        }
    }
}
