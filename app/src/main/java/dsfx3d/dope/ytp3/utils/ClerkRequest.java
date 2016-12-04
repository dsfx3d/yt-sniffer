package dsfx3d.dope.ytp3.utils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 *
 *  @ClerkRequest
 *
 *  entity class for @HyperClerk requests
 * **/


public class ClerkRequest {

    public ClerkRequest() {}

    public L l;
    public String requestUrl;
    public Response.Listener<String> stringResponseListener;
    public Response.Listener<byte[]> byteResponseListener;
    public Response.ErrorListener errorListener;

    public final static int STRING_RESPONSE = 0;
    public final static int BYTE_RESPONSE = 1;

    public ClerkRequest(String requestUrl) {
        l = new L(ClerkRequest.class.getSimpleName());
        this.requestUrl = requestUrl;
    }

    public ClerkRequest(final String requestUrl, final Listener listener, int responseMode) {
        this(requestUrl);
        switch (responseMode) {
            case STRING_RESPONSE:
                addStringResponseListener(listener);
                break;
            case BYTE_RESPONSE:
                addByteResponseListener(listener);
        }
        addErrorResponseListener(listener);
    }

    private void addErrorResponseListener(final Listener listener) {
        this.errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.p("clerk couldn't finish request: Volley Error occurred\n"+error.getMessage(),L.VERBOSE);
            }
        };
    }

    private void addByteResponseListener(final Listener<byte[]> listener) {
        this.byteResponseListener = new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                l.p("clerk responded: byte array received",L.VERBOSE);
                if(listener!=null)  listener.onResponse(response);
            }
        };
    }

    private void addStringResponseListener(final Listener<String> listener) {
        this.stringResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.p("clerk responded:\n"+response,L.VERBOSE);
                if(listener!=null)  listener.onResponse(response);
            }
        };
    }

    public interface Listener<T> {
        public void onResponse(T request);
        public void onErrorResponse(VolleyError error);

    }
}
