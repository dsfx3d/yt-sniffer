package dsfx3d.dope.ytp3.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
    @HyperClerk
    http communicator on @Volley
 */

public class HyperClerk {
    private L l;
    private Context context;

    private RequestQueue requestQueue;

    public HyperClerk(Context context) {
        this.context = context;
        l = new L(this.getClass().getSimpleName());
        reset();
        l.p("new HyperClerk hired!",L.VERBOSE);
    }

    private void reset() {
        l.p("resetting request queue..",L.VERBOSE);
        requestQueue = Volley.newRequestQueue(context);
    }

    public void addStringRequest(final ClerkRequest request) {
        if(request==null)return;
        l.p("adding request:\n { "+request.requestUrl+" } ",L.VERBOSE);
        StringRequest req = new StringRequest(request.requestUrl, request.stringResponseListener, request.errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if( request.requestUrl.contains("mashape.com")) {
                    Map<String, String> params = new HashMap<>();
                    params.put("X-Mashape-Key", "Obs3xZj88Wmshe7FttuUIPDvhRcUp1KcAB4jsnu475laAPa04l");
                    return params;
                }
                return super.getHeaders();
            }
        };
        requestQueue.add(req);
    }

    public void addByteResponse(final ClerkRequest request) {
        if(request==null)return;
        l.p("adding request:\n { "+request.requestUrl+" } ",L.VERBOSE);
        Request<byte[]> req = new Request<byte[]>(Request.Method.GET, request.requestUrl, request.errorListener) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                return null;
            }

            @Override
            protected void deliverResponse(byte[] response) {
                l.p("array response recived",L.VERBOSE);
            }
        };
        requestQueue.add(req);
    }

    public void startListening() {
        l.p("HyperClerk is listening request queue..",L.VERBOSE);
        requestQueue.start();
    }

    public void halt() {
        requestQueue.stop();
        l.p("HYPERCLERK HALTED !!",L.ERROR);
    }
}
