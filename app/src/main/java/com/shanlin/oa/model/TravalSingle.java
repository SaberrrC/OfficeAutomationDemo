package com.shanlin.oa.model;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by CXP on Date: 2016/11/22 0022 下午 10:27.
 * Description:出差单个实例
 */

public class TravalSingle {
    String vehicle;
    String address;
    String start_time;
    String end_time;

    public TravalSingle(String address, String start_time, String end_time, String vehicle) {
        this.address = address;
        this.start_time = start_time;
        this.end_time = end_time;
        this.vehicle = vehicle;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
