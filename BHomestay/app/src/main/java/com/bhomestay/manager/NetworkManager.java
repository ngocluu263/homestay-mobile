package com.bhomestay.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bhomestay.application.Application;
import com.bhomestay.model.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Map;


/**
 * Created by suninguyen on 10/18/16.
 */
public class NetworkManager {
    private static final String LOG_TAG = NetworkManager.class.getSimpleName();

    //Constant
    private static final int TIME_OUT = 30000;
    private static final int BUFFER_SIZE_DEFAULT = 4096;
    private static final String CHARSET_DEFAULT = "UTF-8";
    public static final String NET_SUPPORT_WIFI = "WIFI";
    public static final String NET_SUPPORT_MOBILE = "3G";
    public static final String[] NET_SUPPORT = {NetworkManager.NET_SUPPORT_WIFI, NetworkManager.NET_SUPPORT_MOBILE};


    /**
     * This method is used for checking network connected or not.
     *
     * @return true: if connected otherwise false.
     */
    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = getConnectedNetwork(context);
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    /**
     * This methed is used to getInfo from network
     * @param context
     * @return
     */
    public static NetworkInfo getConnectedNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * This method is used to execute a POST request to a specified URL
     *
     * @param urlString The specified URL to call POST request to
     * @param params    The parameters to append to the URL
     * @return a response object contain response information from server
     */
    public Response executePostRequest(String urlString, Map<String, String> params, Map<String, String> headers) {
        List<NameValuePair> lstParam = new ArrayList<>();
        for (String key : params.keySet()) {
            lstParam.add(new BasicNameValuePair(key, params.get(key)));
        }
        return executePostRequest(urlString, lstParam, headers);
    }


    /**
     * This method is used to execute a POST request to a specified URL
     *
     * @param urlString The specified URL to call POST request to
     * @param params    The parameters to append to the URL
     * @return a response object contain response information from server
     */
    public Response executePostRequest(String urlString, List<NameValuePair> params, Map<String, String> headerParams) {
        HttpURLConnection httpUrlCon = null;
        Response response = new Response(Response.STATUS_UNKNOWN_ERROR);

        // Check network open
        if (!isConnected(Application.getInstance())) {
            // Network disabled
            response.setStatus(Response.STATUS_REQUEST_NETWORK_DISABLED);
            Log.d(LOG_TAG, "[executePostRequest] Network disabled");
            return response;
        }
        try {
            // Make url
            URL url = new URL(urlString);
            Log.d(LOG_TAG, "[executePostRequest] Call url... " + url + " with params: " + params);
            httpUrlCon = executeURL(url, params, headerParams, true);
            switch (getResponseCode(httpUrlCon)) {
                case Response.STATUS_BAD_REQUEST:
                    //authen bad request
                    //TODO re-login if needed
                    break;
                case Response.STATUS_UNKNOWN_ERROR:
                    response.setStatus(Response.STATUS_UNKNOWN_ERROR);
                    return response;
                default:
                    response = getResponse(httpUrlCon);
                    break;
            }
        } catch (SocketTimeoutException | FileNotFoundException | UnknownHostException | JsonSyntaxException e) {
            // Cannot ping to host - timed out
            response.setStatus(Response.STATUS_REQUEST_TIME_OUT);
            Log.w(LOG_TAG, "[executePostRequest] Request timeout: " + e);
        } catch (IOException e) {
            Log.w(LOG_TAG, "[executePostRequest] Unknown exception: " + e);
        } finally {
            // Save to release resource
            releaseResourceSafely(httpUrlCon);
        }

        Log.w(LOG_TAG, "[executePostRequest] Request response: " + response);
        return response;
    }


    public HttpURLConnection executeURL(URL url, List<NameValuePair> bodyParams, Map<String, String> headerParams, boolean needAuthorization) throws IOException {
        // Open connection
        HttpURLConnection httpUrlCon = (HttpURLConnection) url.openConnection();
        httpUrlCon.setConnectTimeout(TIME_OUT);
        httpUrlCon.setReadTimeout(TIME_OUT);
        // Set method
        httpUrlCon.setRequestMethod("POST");
        httpUrlCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpUrlCon.setDoInput(true);
        httpUrlCon.setDoOutput(true);
        httpUrlCon.setUseCaches(false);


        //TODO authorization if needed
        Log.d(LOG_TAG,"needAuthorization:" + needAuthorization);

        if (headerParams != null && headerParams.size() > 0) {
            for (String headerKey : headerParams.keySet()) {
                httpUrlCon.setRequestProperty(headerKey, headerParams.get(headerKey));
            }
        }

        // Add parameter if any
        if (bodyParams != null) {
            OutputStream outputStream = null;
            try {
                // Begin send param to server
                String strParams = URLEncodedUtils.format(bodyParams, CHARSET_DEFAULT);
                outputStream = httpUrlCon.getOutputStream();
                outputStream.write(strParams.getBytes(CHARSET_DEFAULT));
                outputStream.flush();
            } catch (IOException e) {
                Log.d(LOG_TAG, "[executeURL] Write param error: " + e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
        return httpUrlCon;
    }

    public int getResponseCode(HttpURLConnection httpUrlCon) {
        int responseCode = Response.STATUS_UNKNOWN_ERROR;
        try {
            responseCode = httpUrlCon.getResponseCode();
        } catch (IOException e) {
            Log.w(LOG_TAG, "[executePostRequest] getResponseCode exception: " + e);
            // Assume IOException: No authentication challenges found
            responseCode = Response.STATUS_UNKNOWN_ERROR;
        }
        Log.w(LOG_TAG, "[getResponseCode] responseCode: " + responseCode);
        return responseCode;
    }

    private Response getResponse(HttpURLConnection httpUrlCon) throws IOException {
        // Begin receive response json body from server
        InputStream is = httpUrlCon.getInputStream();
        byte[] data = new byte[BUFFER_SIZE_DEFAULT];
        int length;
        StringBuilder sb = new StringBuilder();
        while ((length = is.read(data)) != -1) {
            sb.append(new String(data, 0, length));
        }
        // Close all stream
        is.close();
        // Convert to Response
        return new Gson().fromJson(sb.toString(), Response.class);
    }

    /**
     * This method to close all open resource, include: connection, Closeable (ex: stream) ....
     *
     * @param httpURLConnection Connection need to close
     * @param closeables        Closeable (ex: stream) need to close
     * @return Is close success status
     */
    public static boolean releaseResourceSafely(HttpURLConnection httpURLConnection, Closeable... closeables) {
        boolean isSuccessReleased = false;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
            isSuccessReleased = true;
        } catch (IOException e) {
            Log.w(LOG_TAG, "[releaseResourceSafely] IOException: " + e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return isSuccessReleased;
    }

}
