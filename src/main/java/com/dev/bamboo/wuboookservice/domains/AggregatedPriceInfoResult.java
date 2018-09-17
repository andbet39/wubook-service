package com.dev.bamboo.wuboookservice.domains;


import java.util.Date;

public class AggregatedPriceInfoResult {

    private double avg;
    private float max;
    private float min;

    private Date date;

    public AggregatedPriceInfoResult(double avg, float max, float min, Date date) {
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.date = date;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AggregatedPriceInfoResult{" +
                "avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", date=" + date +
                '}';
    }
}
