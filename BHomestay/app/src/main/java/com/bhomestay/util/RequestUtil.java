package com.bhomestay.util;

import com.bhomestay.listener.RequestListener;
import com.bhomestay.task.RequestTask;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by suninguyen on 10/18/16.
 */
public final class RequestUtil {
    // Config about server network here
    //CHÚ Ý : Không được để thằng Toàn bóp
    public static final String URL_SERVER_BASE = "chú ý không được để thằng Toàn bóp";

    /**
     * Declare url
     */
    //Health Check
    public static final String URL_SERVER_LOGOUT = URL_SERVER_BASE + "/user/logout";
    private static final String URL_SERVER_HEALTH_CHECK = URL_SERVER_BASE + "checkHealth";

    //Login
    public static final String URL_SERVER_LOGIN = URL_SERVER_BASE + "/login";
    //Register
    public static final String URL_SERVER_REGISTER = URL_SERVER_BASE + "/register";
    //Activate
    public static final String URL_SERVER_ACTIVATE = URL_SERVER_BASE + "/activate";
    //Reset Password
    public static final String URL_SERVER_RESETPASSWORD = URL_SERVER_BASE + "/resetPassword";
    //Change Password
    public static final String URL_SERVER_CHANGEPASSWORD = URL_SERVER_BASE + "/changePassword";

    /**
     * PARAM & VALUES
     */
    //Login
    public static final String PARAM_SERVER_LOGIN_EMAIL = "email";
    public static final String PARAM_SERVER_LOGIN_PASSWORD = "password";
    //Register
    public static final String PARAM_SERVER_REGISTER_EMAIL = "email";
    public static final String PARAM_SERVER_REGISTER_PASSWORD = "password";
    public static final String PARAM_SERVER_REGISTER_FIRSTNAME = "firstName";
    public static final String PARAM_SERVER_REGISTER_LASTNAME = "lastName";
    //Activate
    public static final String PARAM_SERVER_ACTIVATE_EMAIL = "email";
    public static final String PARAM_SERVER_ACTIVATE_ACTIVATECODE = "activateCode";
    //Reset Password
    public static final String PARAM_SERVER_RESETPASSWORD_EMAIL = "email";
    //Change Password
    public static final String PARAM_SERVER_CHANGEPASSWORD_EMAIL = "email";
    public static final String PARAM_SERVER_CHANGEPASSWORD_RESETCODE = "resetCode";
    public static final String PARAM_SERVER_CHANGEPASSWORD_NEWPASSWORD = "newPassword";
    //Add TCU WizardSetting
    private static RequestTask addTCUTask;


    private RequestUtil() {

    }

    public static void login(RequestListener requestListener, String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_LOGIN_EMAIL, email);
        params.put(PARAM_SERVER_LOGIN_PASSWORD, password);
        new RequestTask(requestListener, params).execute(URL_SERVER_LOGIN);
    }

    public static void register(RequestListener requestListener, String email, String password, String firstName, String lastName){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_REGISTER_EMAIL, email);
        params.put(PARAM_SERVER_REGISTER_PASSWORD, password);
        params.put(PARAM_SERVER_REGISTER_FIRSTNAME, firstName);
        params.put(PARAM_SERVER_REGISTER_LASTNAME, lastName);
        new RequestTask(requestListener, params).execute(URL_SERVER_REGISTER);
    }

    public static void activate(RequestListener requestListener, String email, String activateCode){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_ACTIVATE_EMAIL, email);
        params.put(PARAM_SERVER_ACTIVATE_ACTIVATECODE, activateCode);
        new RequestTask(requestListener, params).execute(URL_SERVER_ACTIVATE);
    }

    public static void resetPassword(RequestListener requestListener, String email) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_RESETPASSWORD_EMAIL, email);
        new RequestTask(requestListener, params).execute(URL_SERVER_RESETPASSWORD);
    }

    public static void changePassword(RequestListener requestListener, String email, String resetCode, String newPassword){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_CHANGEPASSWORD_EMAIL, email);
        params.put(PARAM_SERVER_CHANGEPASSWORD_RESETCODE, resetCode);
        params.put(PARAM_SERVER_CHANGEPASSWORD_NEWPASSWORD, newPassword);
        new RequestTask(requestListener, params).execute(URL_SERVER_CHANGEPASSWORD);
    }

    public static void logout(final RequestListener requestListener) {
        Map<String, String> params = new HashMap<>();
        new RequestTask(requestListener, new HashMap<String, String>(), params).execute(URL_SERVER_LOGOUT);
    }
    
    public static void checkServerConnection(final RequestListener requestListener) {
        Map<String, String> params = new HashMap<>();
        new RequestTask(requestListener, new HashMap<String, String>(), params).execute(URL_SERVER_HEALTH_CHECK);
    }

    
}
