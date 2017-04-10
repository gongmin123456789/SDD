package com.gm.sdd.http;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import fi.iki.elonen.NanoHTTPD;

/**
 * Created by 80066158 on 2017-04-10.
 */

public class MyHttpServer extends NanoHTTPD {
    private static final String TAG = "MyHttpServer";

    private static final String URI_TYPE_GET_ICON = "/favicon.ico";

    private int iconId = 0;
    private Context context = null;

    public MyHttpServer(int port, Context context) {
        super(port);
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.i(TAG, "<serve> " + session.getMethod().name() + ", " + session.getUri());

        Response response = null;
        if (Method.GET.equals(session.getMethod())) {
            response = getHandler(session);
        } else if (Method.POST.equals(session.getMethod())) {
            response = postHandler(session);
        }

        if (null != response) {
            return response;
        }

        return super.serve(session);
    }

    public static String getIconUri() {
        return URI_TYPE_GET_ICON;
    }

    public void setIcon(int iconId) {
        this.iconId = iconId;
    }

    private Response getHandler(IHTTPSession session) {
        Log.i(TAG, "<getHandler> start");

        Response response = null;

        String uri = session.getUri();
        if (null == uri ||
                uri.equals("")) {
            return null;
        }

        if (uri.equals(URI_TYPE_GET_ICON)) {
            response = getIconResponse();
        }

        return response;
    }

    private Response postHandler(IHTTPSession session) {
        Log.i(TAG, "<postHandler> start");

        Response response = null;

        return response;
    }

    private Response getIconResponse() {
        Log.i(TAG, "<getIconResponse> start");
        if (0 == iconId) {
            return null;
        }

        InputStream inputStream = context.getResources().openRawResource(iconId);
        Response response = new Response(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, inputStream);
        return response;
    }
}
