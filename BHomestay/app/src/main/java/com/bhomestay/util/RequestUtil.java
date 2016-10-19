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

    //Health Check
    public static final String URL_SERVER_LOGOUT = URL_SERVER_BASE + "/user/logout";
    private static final String URL_SERVER_HEALTH_CHECK = URL_SERVER_BASE + "checkHealth";

    //Add TCU WizardSetting
    private static RequestTask addTCUTask;


    private RequestUtil() {

    }


    public static void login(RequestListener requestListener, String id, String password) {
//        Map<String, String> params = new HashMap<>();
//        params.put(PARAM_SERVER_LOGIN_ID, id);
//        params.put(PARAM_SERVER_LOGIN_PASSWORD, password);
//        new RequestTask(requestListener, params).execute(URL_SERVER_LOGIN);
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
