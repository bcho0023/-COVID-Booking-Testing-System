package Screens;

import Models.BookingPackage.Booking;
import Models.DataModel;
import Models.SitePackage.Site;
import Models.UserPackage.User;
import Screens.BookingScreens.BookingController;
import Screens.BookingScreens.BookingView;
import Screens.SiteScreens.SiteController;
import Screens.SiteScreens.SiteView;
import Screens.SystemScreens.SystemController;
import Screens.SystemScreens.SystemView;
import Screens.UserScreens.UserController;
import Screens.UserScreens.UserView;

public interface AbstractFactory {
    // Create new controller instance with default view
    public BookingController getBookingController();
    public SystemController getSystemController();
    public UserController getUserController();
    public SiteController getSiteController();

    // Create view with specified model
    public BookingView getBookingView(Booking model);
    public SystemView getSystemView(DataModel model);
    public UserView getUserView(User model);
    public SiteView getSiteView(Site model);
}
