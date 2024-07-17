package Actions;

import Screens.FactoryCreator;

public class CreateBookingOnsiteAction implements Action {
    @Override
    public String name() {
        return "Make booking for customer";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getBookingController().createOnsite();
    }
}
