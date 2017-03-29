package com.gm.sdd.common;

/**
 * Created by 80066158 on 2017-03-29.
 */

public interface IOnRemoteDataReceiveListener {
    public void onRemoteDataReceive(String remoteIp, int remotePort, String remoteData);
}
