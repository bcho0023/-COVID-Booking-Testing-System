package Actions;

import Screens.FactoryCreator;

public class ProfileAction implements Action {
    @Override
    public String name() {
        return "View profile";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getUserController().profile();
    }
}
