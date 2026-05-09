package Screens;

import models.Guest;
import models.Reservation;
import models.Room;
import models.Staff;

public final class SessionContext {
    private SessionContext() {}

    public enum LoginMode {
        GUEST,
        STAFF
    }

    public static Guest currentGuest;
    public static Staff currentStaff;
    public static String currentPassword;

    public static Room selectedRoom;
    public static Reservation selectedReservation;

    public static LoginMode selectedLoginMode;

    public static void setLoginMode(LoginMode mode) {
        selectedLoginMode = mode;
        currentGuest = null;
        currentStaff = null;
        currentPassword = null;
        selectedRoom = null;
        selectedReservation = null;
    }

    public static void loginGuest(Guest guest, String password) {
        currentGuest = guest;
        currentStaff = null;
        currentPassword = password;
        selectedLoginMode = LoginMode.GUEST;
    }

    public static void loginStaff(Staff staff, String password) {
        currentStaff = staff;
        currentGuest = null;
        currentPassword = password;
        selectedLoginMode = LoginMode.STAFF;
    }

    public static void logout() {
        currentGuest = null;
        currentStaff = null;
        currentPassword = null;
        selectedRoom = null;
        selectedReservation = null;
        selectedLoginMode = null;
    }

    public static boolean isGuestLoginMode() {
        return selectedLoginMode == LoginMode.GUEST;
    }

    public static boolean isStaffLoginMode() {
        return selectedLoginMode == LoginMode.STAFF;
    }

    public static boolean isGuestLoggedIn() {
        return currentGuest != null;
    }

    public static boolean isStaffLoggedIn() {
        return currentStaff != null;
    }
}
