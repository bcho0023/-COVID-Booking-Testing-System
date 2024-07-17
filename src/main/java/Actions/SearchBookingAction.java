package Actions;


import Screens.FactoryCreator;

public class SearchBookingAction implements Action {
    @Override
    public String name() {
        return "Search booking";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getBookingController().search();
    }
}
