package com.gm.sdd.client;

import com.gm.sdd.common.SDDDevice;

/**
 * Created by 80066158 on 2017-03-17.
 */

public interface IOnDeviceChangeListener {
    public void onDeviceChange(final SDDDevice newAddedDevice);
}
