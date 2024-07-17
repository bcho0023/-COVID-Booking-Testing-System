package Actions;

import Screens.FactoryCreator;

public class DeleteBookingAction implements Action {
    @Override
    public String name() {
        return "Delete booking";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getBookingController().delete();
    }
}
