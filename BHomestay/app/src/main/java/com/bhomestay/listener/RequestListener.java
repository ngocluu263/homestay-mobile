package com.bhomestay.listener;


import com.bhomestay.model.Response;

/**
 * Created by suninguyen on 10/18/16.
 */
public interface RequestListener {

    void onPrepareRequest();
    void onRequestDone(Response response);
}
