package Actions;


import Screens.FactoryCreator;

public class SearchSiteAction implements Action {
    @Override
    public String name() {
        return "Search for testing sites";
    }

    @Override
    public void execute() {
        FactoryCreator.getFactory("CONSOLE").getSiteController().search();
    }
}
