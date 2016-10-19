package com.bhomestay.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by suninguyen on 10/18/16.
 */
public class Response {
    // Success
    public static final int STATUS_OK = 200;
    // Unknown error
    public static final int STATUS_UNKNOWN_ERROR = -1;
    // Network disabled - Mobile don't enable network
    public static final int STATUS_REQUEST_NETWORK_DISABLED = -2;
    // Time out - Host not reachable - Cannot ping to host
    public static final int STATUS_REQUEST_TIME_OUT = -3;
    // Bad request
    public static final int STATUS_BAD_REQUEST = 400;

    public Response(int status) {
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
