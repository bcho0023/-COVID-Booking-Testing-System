package Actions;

import Screens.FactoryCreator;

public class CreateBookingSystemAction implements Action {
    @Override
    public String name() {
        return "Make booking online";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getBookingController().createSystem();
    }
}
