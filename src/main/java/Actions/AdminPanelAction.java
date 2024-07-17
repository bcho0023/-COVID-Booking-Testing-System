package Actions;

import Screens.FactoryCreator;

public class AdminPanelAction implements Action {
    @Override
    public String name() {
        return "View admin booking interface";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getUserController().adminPanel();
    }
}
