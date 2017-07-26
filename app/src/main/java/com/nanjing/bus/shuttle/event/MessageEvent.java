package com.nanjing.bus.shuttle.event;

import android.os.Bundle;

import com.baidu.location.BDLocation;

/**
 * Created by fylder on 2017/3/11.
 */

public class MessageEvent {

    BDLocation location;

    int flag;

    public MessageEvent(BDLocation location, int flag) {
        this.location = location;
        this.flag = flag;
    }

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
