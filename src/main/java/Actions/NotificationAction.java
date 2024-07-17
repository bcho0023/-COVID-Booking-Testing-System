package Actions;


import Screens.FactoryCreator;

public class NotificationAction implements Action {
    @Override
    public String name() {
        return "View current notifications";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getUserController().notifications();
    }
}
