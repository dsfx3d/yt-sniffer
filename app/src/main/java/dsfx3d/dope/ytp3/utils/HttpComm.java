package dsfx3d.dope.ytp3.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 11/22/2016.
 */

public class HttpComm {
    private RequestQueue requestQueue;
    private String url = "https://kashyap32-youtubetomp3-v1.p.mashape.com/";

    public HttpComm() {

    }

    public HttpComm(Context context) {
        this();
        requestQueue = Volley.newRequestQueue(context);
    }

    public void addRequest(int method, String url, Response.Listener<String> responseListener, Response.ErrorListener errListener) {
        this.url+=url;
        requestQueue.add(new StringRequest(method, this.url, responseListener, errListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("X-Mashape-Key","wVl3Ry7iOZmshYH6uMNu1drUfEump1YkNDwjsnenNxkh1ZzaRG");
                params.put("Accept","text/plain");
                return params;
            }
        });
    }

    public void startRequestPipeline() {
        requestQueue.start();
    }

    public void stopResquestPipeline() {
        requestQueue.stop();
    }


}
