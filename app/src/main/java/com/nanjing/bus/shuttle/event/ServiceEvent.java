package com.nanjing.bus.shuttle.event;

/**
 * Created by fylder on 2017/3/11.
 */

public class ServiceEvent {

    private int flag;

    public ServiceEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
