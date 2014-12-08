package ncucsie.cas;


public interface Constant {
    public final static String SERVER_DOMAIN_NAME = "w181496.twbbs.org";
    public final static String SERVER_ROOT = "/curl_test/";
    public final static int SERVER_PORT = 80;
    //TODO: some of the constants here shall be able to be overridden by configuration files

    public final static String DEBUG_TAG = "Debug:";


    public final static int VIEW_APPOINTMENT = 0;
    public final static int ADD_APPOINTMENT = 1;
    public final static int MODIFY_APPOINTMENT = 2;
    public final static int DELETE_APPOINTMENT = 3;
    public final static int MY_APPOINTMENT = 4;
    public final static int LOGIN = 5;
    public final static int LOGOUT = 6;

    public final static String VIEW_APPOINTMENT_PAGE = "viewAppointment.php";
    public final static String ADD_APPOINTMENT_PAGE = "addAppointment.php";
    public final static String MODIFY_APPOINTMENT_PAGE = "modifyAppointment.php";
    public final static String DELETE_APPOINTMENT_PAGE = "deleteAppointment.php";
    public final static String MY_APPOINTMENT_PAGE = "myAppointment.php";
    public final static String LOGIN_PAGE = "login.php";
    public final static String LOGOUT_PAGE = "logout.php";


    public final static String USER_EXTRA = "com.ncucsie.cas.user_extra";
}
