package com.shanlinjinrong.oa.model;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by CXP on Date: 2016/9/8 11:56.
 * Description:消息头部实体类,包含message和notice
 */
public class MessageHeader {

    public class Message {
        public int count;
        public String createtime;
        public String title;

    }

    public class notice {
        public int count;
        public String createtime;
        public String title;
    }
}
