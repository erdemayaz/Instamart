package com.ayaz.instamart.marttool;

/**
 * Created by Ayaz on 17.11.2016.
 */

public class Constant {
    // Server Constants
    public static final String HOST_IP ="";
    public static final String DOMAIN_NAME = "http://instamart.hol.es/";
    public static final String API_PATH = "api/";
    public static final String LOGIN = "login.php";
    public static final String SIGN = "register.php";
    public static final String LOGOUT = "logout.php";
    public static final String PRESENTATION = "presentation.php";
    public static final String PUBLICATION = "uploadproduct.php";
    public static final String DEMAND_PRESENTATION = "demandpresentation.php";
    public static final String DEMAND_PUBLICATION = "uploaddemand.php";

    // Location Constants
    public static final String LOCATION_UPDATED = "location_updated";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    // Http Constants
    public static final char PARAMETER_EQUALS = '=';
    public static final char PARAMETER_DELIMITER = '&';
    public static final char PARAMETER_QUEUE_DELIMITER = '?';
    public static final int TIMEOUT_IN_MS = 60000;

    // Permission Constants
    public static final int PERMISSION_CAMERA_CODE = 1;
    public static final int PERMISSION_LOCATION_CODE = 2;

    // Duration Constants
    public class Time{
        public static final long SECOND = 1000;
        public static final long MINUTE = 60000;
        public static final long HOUR = 3600000;
        public static final long DAY = 86400000;
        public static final long WEEK = 604800000;
    }
}