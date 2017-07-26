package com.nanjing.bus.shuttle.http.model;

/**
 * Created by fylder on 2017/3/11.
 */

public class LineModel {

    String line;
    double lat;
    double lon;
    long time;
    float speed;

    public LineModel() {
    }

    public LineModel(String line, double lat, double lon, long time, float speed) {
        this.line = line;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.speed = speed;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "LineModel{" +
                "line='" + line + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", time=" + time +
                ", speed=" + speed +
                '}';
    }
}
