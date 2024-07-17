package Screens;

import Models.BookingPackage.Booking;
import Models.DataModel;
import Models.SitePackage.Site;
import Models.UserPackage.User;
import Screens.BookingScreens.BookingController;
import Screens.BookingScreens.BookingView;
import Screens.BookingScreens.BookingViewConsole;
import Screens.SiteScreens.SiteController;
import Screens.SiteScreens.SiteView;
import Screens.SiteScreens.SiteViewConsole;
import Screens.SystemScreens.SystemController;
import Screens.SystemScreens.SystemView;
import Screens.SystemScreens.SystemViewConsole;
import Screens.UserScreens.UserController;
import Screens.UserScreens.UserView;
import Screens.UserScreens.UserViewConsole;

public class ConsoleFactory implements AbstractFactory {
    // Create new controller instance
    @Override
    public BookingController getBookingController() {
        return new BookingController(this);
    }

    @Override
    public SystemController getSystemController() {
        return new SystemController(this);
    }

    @Override
    public UserController getUserController() {
        return new UserController(this);
    }

    @Override
    public SiteController getSiteController() {
        return new SiteController(this);
    }

    // Create view with specified model
    @Override
    public BookingView getBookingView(Booking model) {
        return new BookingViewConsole(model);
    }

    @Override
    public SystemView getSystemView(DataModel model) {
        return new SystemViewConsole(model);
    }

    @Override
    public UserView getUserView(User model) {
        return new UserViewConsole(model);
    }

    @Override
    public SiteView getSiteView(Site model) {
        return new SiteViewConsole(model);
    }
}
