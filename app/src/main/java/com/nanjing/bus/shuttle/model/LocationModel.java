package com.nanjing.bus.shuttle.model;

import com.baidu.location.Address;

/**
 * Created by fylder on 2017/3/11.
 */

public class LocationModel {

    Address address;
    double longitude;
    double latitude;
    float speed;
    String time;


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "address=" + address.address +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", speed=" + speed +
                ", time='" + time + '\'' +
                '}';
    }
}
