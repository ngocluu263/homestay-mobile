package com.bhomestay.task;

import android.os.AsyncTask;
import android.util.Log;

import com.bhomestay.listener.RequestListener;
import com.bhomestay.manager.NetworkManager;
import com.bhomestay.model.Response;

import java.util.Map;



/**
 * COMMON CLASS
 * This class is used to share common function
 *
 *  Created by suninguyen on 10/18/16.
 */
public class RequestTask extends AsyncTask<String, Void, Response> {
    private RequestListener requestListener;
    private Map<String, String> params;
    private Map<String, String> headerParams;

    public RequestTask(RequestListener requestListener, Map<String, String> params) {
        this.requestListener = requestListener;
        this.params = params;
    }

    public RequestTask(RequestListener requestListener, Map<String, String> params, Map<String, String> headerParams) {
        this.requestListener = requestListener;
        this.params = params;
        this.headerParams = headerParams;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        requestListener.onPrepareRequest();
    }

    @Override
    protected Response doInBackground(String... params) {
        String url = params[0];
        NetworkManager executor = new NetworkManager();
        return executor.executePostRequest(url, this.params, this.headerParams);
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        requestListener.onRequestDone(response);
    }

    @Override
    protected void onCancelled() {
        Log.d("onCancelled", "onCancelled 1");
        super.onCancelled();
        requestListener = new RequestListener() {
            @Override
            public void onPrepareRequest() {

            }

            @Override
            public void onRequestDone(Response response) {

            }
        };
    }

    @Override
    protected void onCancelled(Response response) {
        Log.d("onCancelled" , "onCancelled 2");
        super.onCancelled(response);
        requestListener = new RequestListener() {
            @Override
            public void onPrepareRequest() {

            }

            @Override
            public void onRequestDone(Response response) {

            }
        };
    }
}
