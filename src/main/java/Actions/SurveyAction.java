package Actions;


import Screens.FactoryCreator;

public class SurveyAction implements Action {
    @Override
    public String name() {
        return "Perform covid test survey for patient";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getBookingController().survey();
    }
}