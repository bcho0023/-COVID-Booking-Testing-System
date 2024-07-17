package Actions;


import Screens.FactoryCreator;

public class ModifyBookingSystemAction implements Action {
    @Override
    public String name() {
        return "Modify booking online";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getBookingController().modifySystem();
    }
}
