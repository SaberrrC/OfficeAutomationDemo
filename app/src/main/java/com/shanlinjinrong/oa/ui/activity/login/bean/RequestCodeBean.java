package com.shanlinjinrong.oa.ui.activity.login.bean;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/29.
 */

public class RequestCodeBean implements Serializable {

    /**
     * code : 000000
     * message : 获取验证码成功
     * data : {"keyCode":"3cef6a85b1f0930e880e24d272a1c814","img":"R0lGODlhZAAeAPAAAAAAAP///ywAAAAAZAAeAEAI5AABCBxIsKDBgwgTKlzIsGHDABAjSnRIcWFEABIDVLw4cePFigI/DoRIsSNGkQxRglyZkKRJljBjIjSpUSZIlTZZvszpseZIkg+Bwty5kijPo0iTKl36M6NPplBzGo1KECdVg06nQs3YtKRQnVavdq36VWFYr2XRakxr0aXWg2cdvg3KVqzdoAXj3uXJda/flFn96rU7N+pgwoeZJp4JdPHJrE8RQ1YbuWfluy8TO2ZcV6xKzZ3lbn4ceTFK06HpXqb707JTtFJHN5Vtk/bs2rb/Gu6ruzdWn7l9xy4dXLjx4wACAgA7"}
     */

    private String code;
    private String   message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * keyCode : 3cef6a85b1f0930e880e24d272a1c814
         * img : R0lGODlhZAAeAPAAAAAAAP///ywAAAAAZAAeAEAI5AABCBxIsKDBgwgTKlzIsGHDABAjSnRIcWFEABIDVLw4cePFigI/DoRIsSNGkQxRglyZkKRJljBjIjSpUSZIlTZZvszpseZIkg+Bwty5kijPo0iTKl36M6NPplBzGo1KECdVg06nQs3YtKRQnVavdq36VWFYr2XRakxr0aXWg2cdvg3KVqzdoAXj3uXJda/flFn96rU7N+pgwoeZJp4JdPHJrE8RQ1YbuWfluy8TO2ZcV6xKzZ3lbn4ceTFK06HpXqb707JTtFJHN5Vtk/bs2rb/Gu6ruzdWn7l9xy4dXLjx4wACAgA7
         */

        private String keyCode;
        private String img;

        public String getKeyCode() {
            return keyCode;
        }

        public void setKeyCode(String keyCode) {
            this.keyCode = keyCode;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
