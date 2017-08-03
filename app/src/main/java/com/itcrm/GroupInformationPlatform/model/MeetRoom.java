package com.itcrm.GroupInformationPlatform.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/18.
 */

public class MeetRoom implements MultiItemEntity,Serializable {
    private String room_id;
    private String roomname;
    private String address;
    private String floor;
    private String device;
    private String nop;
    private String begintime;

    public MeetRoom(String room_id, String roomname, String address, String floor, String device, String nop, String begintime) {
        this.room_id = room_id;
        this.roomname = roomname;
        this.address = address;
        this.floor = floor;
        this.device = device;
        this.nop = nop;
        this.begintime = begintime;
    }
    public MeetRoom(JSONObject jsonObject) {
        try {
            room_id = jsonObject.getString("room_id");
            roomname = jsonObject.getString("roomname");
            address = jsonObject.getString("address");
            floor = jsonObject.getString("floor");
            device = jsonObject.getString("device");
            nop = jsonObject.getString("nop");
            begintime = jsonObject.getString("begintime");

        }catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("user解析异常-》"+e.toString());
        }
    }

    @Override
    public String toString() {
        return "MeetRoom{" +
                "room_id='" + room_id + '\'' +
                ", roomname='" + roomname + '\'' +
                ", address='" + address + '\'' +
                ", floor='" + floor + '\'' +
                ", device='" + device + '\'' +
                ", nop='" + nop + '\'' +
                ", begintime='" + begintime + '\'' +
                '}';
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getNop() {
        return nop;
    }

    public void setNop(String nop) {
        this.nop = nop;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    @Override
    public int getItemType() {
        return 10;
    }
}
