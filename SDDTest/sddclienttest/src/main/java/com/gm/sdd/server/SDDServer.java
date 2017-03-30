package com.gm.sdd.server;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gm.sdd.common.SDDDevice;

/**
 * Created by 80066158 on 2017-03-17.
 */

public class SDDServer {
    private static final String TAG = "SDDServer";

    private SDDDevice device = null;
    private Context context = null;
    private boolean isStarted = false;

    public SDDServer(Context context, SDDDevice device) {
        this.device = device;
    }

    public void start() {
        Log.i(TAG, "<start> isStarted = " + isStarted);

        if (isStarted) {
            return;
        }
        isStarted = true;

        Intent intent = new Intent(context, SDDServerService.class);
        intent.putExtra(SDDServerService.INTENT_EXTRA_NAME_SDDDEVICE, device);
        context.startService(intent);
    }

    public void stop() {
        Log.i(TAG, "<stop> isStarted = " + isStarted);

        if (!isStarted) {
            return;
        }
        isStarted = false;

        Intent intent = new Intent(context, SDDServerService.class);
        context.stopService(intent);
    }

    public boolean isStarted() {
        return isStarted;
    }
}
