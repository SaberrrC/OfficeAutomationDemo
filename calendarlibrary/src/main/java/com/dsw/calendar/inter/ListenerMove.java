package com.dsw.calendar.inter;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.dsw.calendar.inter
 * Author:Created by Tsui on Date:2016/12/19 11:06
 * Description:
 */
public interface ListenerMove {
    void leftMove(String year,String month);
    void rightMove(String year,String month);
    void currentDate(String year,String month);
}
