package Actions;


import Screens.FactoryCreator;

public class LoginAction implements Action {
    @Override
    public String name() {
        return "Login";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getSystemController().login();
    }
}
