package Actions;

import Screens.FactoryCreator;

public class ModifyBookingPhoneAction implements Action {
    @Override
    public String name() {
        return "Modify booking for customer";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getBookingController().modifyPhone();
    }
}
