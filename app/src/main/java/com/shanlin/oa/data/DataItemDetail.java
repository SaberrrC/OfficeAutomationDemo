package com.shanlin.oa.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 单条数据容器
 * <p>
 * 1.可存放 String - String 键值对
 * 2.可存放 String - Integer 键值对
 * 3.可存放 String - Boolean 键值对
 * 4.支持序列化和反序列化
 */
public class DataItemDetail {
    private Map<String, String> data;

    /**
     * 构造函数，初始化容器哈希表
     */
    public DataItemDetail() {
        this.data = new HashMap<>();
    }


    /**
     * @return count
     */
    public int getCount() {
        if (null == this.data) {
            return 0;
        }
        return this.data.size();
    }


    /**
     * 判断当前节点是否存在一个键名
     *
     * @return Boolean
     */
    public Boolean hasKey(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }

        return data.containsKey(key);
    }

    /**
     * 判断当前节点是否存在一个键值对
     */
    public Boolean hasKeyValue(String key, String value) {
        if (key == null || key.length() < 1 || value == null) {
            return false;
        }

        if (!this.data.containsKey(key)) {
            return false;
        }

        return value.equals((String) this.data.get(key));
    }


    /**
     * 通过键名取得一个String值
     **/
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * 通过键名取得一个String值
     *
     * @param defaultValue 默认值
     */
    public String getString(String key, String defaultValue) {
        if (key == null || key.length() < 1) {
            return defaultValue;
        }

        if (!this.data.containsKey(key)) {
            return defaultValue;
        }

        String value = this.data.get(key);

        //若key存在，value为null，调用trim（）可能会闪退 这里进行为空的判断
        if (value == null) {
            return defaultValue;
        }
        return value.trim();
    }

    /**
     * 通过键名取得一个 Boolean 值
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 通过键名取得一个 Boolean 值
     *
     * @param defaultValue 默认值
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        if (key == null || key.length() < 1) {
            return defaultValue;
        }

        if (!this.data.containsKey(key)) {
            return defaultValue;
        }

        String value = this.data.get(key);

        if (value.length() < 1) {
            return defaultValue;
        }

        return !value.equals("0") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on");
    }

    /**
     * 通过键名取得一个 int 值 *
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 通过键名取得一个 int 值 *
     *
     * @param defaultValue 默认值
     */
    public int getInt(String key, int defaultValue) {
        if (key == null || key.length() < 1) {
            return defaultValue;
        }

        if (!this.data.containsKey(key)) {
            return defaultValue;
        }

        String value = this.data.get(key);
        int retValue;

        try {
            retValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            retValue = defaultValue;
        }

        return retValue;
    }

    /**
     * 设定一个值类型为Boolean的键值对
     *
     * @param key   键名
     * @param value Boolean类型键值
     * @return Boolean 如果成功返回 value值，可能为true也可能为false；如果失败则恒为false。
     */
    public Boolean setBooleanValue(String key, Boolean value) {
        if (key == null || key.length() < 1) {
            return false;
        }

        this.data.put(key, value ? "1" : "0");

        String res = this.data.get(key);

        return (res != null && res.equals("1"));
    }

    /**
     * 设定一个键值对
     */
    public int setIntValue(String key, int value) {
        if (key == null || key.length() < 1) {
            return 0;
        }

        String strValue = "" + value;

        this.data.put(key, strValue);

        if (!strValue.equals(this.data.get(key))) {
            return 0;
        }

        return value;
    }

    /**
     * 设定一个键值对
     *
     * @return String
     */
    public String setStringValue(String key, String value) {
        if (key == null || key.length() < 1) {
            return null;
        }

        if (value == null) {
            value = "";
        }

        if (value.length() < 0) {
            return "";
        }

        this.data.put(key, value);

        return this.data.get(key);
    }


    /**
     * 判断当前对象是否和另一个对象相同
     *
     * @return boole
     */
    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }

        if (!(o instanceof DataItemDetail)) {
            return false;
        }

        DataItemDetail pO = (DataItemDetail) o;

        if (pO.data.size() != this.data.size()) {
            return false;
        }

        for (String key : data.keySet()) {
            if (!pO.data.containsKey(key)) {
                return false;
            }

            if (!pO.data.get(key).equals(data.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 得到当前键值对维护的哈希表
     */
    public Map<String, String> getAllData() {
        return this.data;
    }

    /**
     * 清除所有元素
     */
    public DataItemDetail clear() {
        this.data.clear();
        return this;
    }

    /**
     * 从另一个 DataItemDetail 追加数据到本对象
     */
    public DataItemDetail append(DataItemDetail item) {
        if (null != item) {
            for (String key : item.data.keySet()) {
                data.put(key, item.data.get(key));
            }
        }
        return this;
    }

}
