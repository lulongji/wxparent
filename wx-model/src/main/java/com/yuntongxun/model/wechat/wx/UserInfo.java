package com.yuntongxun.model.wechat.wx;

import java.util.Arrays;

public class UserInfo {

    private String openId; // 用户的标识
    private String city;// 用户所在城市
    private String[] privilege;
    private String nickname;// 昵称
    private String country;// 用户所在国家
    private String language;// 用户的语言，简体中文为zh_CN
    private String unionid;//
    private String headImgUrl;// 用户头像
    private int sex;// 用户的性别（1是男性，2是女性，0是未知）
    private String province;// 用户所在省份

    private int subscribe;// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
    private String subscribeTime;// 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间

    private String chinalId;//渠道

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String[] privilege) {
        this.privilege = privilege;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getChinalId() {
        return chinalId;
    }

    public void setChinalId(String chinalId) {
        this.chinalId = chinalId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "openId='" + openId + '\'' +
                ", city='" + city + '\'' +
                ", privilege=" + Arrays.toString(privilege) +
                ", nickname='" + nickname + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", unionid='" + unionid + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", sex=" + sex +
                ", province='" + province + '\'' +
                ", subscribe=" + subscribe +
                ", subscribeTime='" + subscribeTime + '\'' +
                ", chinalId='" + chinalId + '\'' +
                '}';
    }
}
