package Actions;

import Screens.FactoryCreator;

public class MenuAction implements Action {
    @Override
    public String name() {
        return "Menu";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getSystemController().menu();
    }
}
