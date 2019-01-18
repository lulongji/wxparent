package com.yuntongxun.model.constants;

/**
 * @Description:微信接口调用接口常量
 * @Author: lulongji
 * @Date: Created in 18:44 2018/11/12
 */
public interface WxConstant {


    /********************************************************微信常量*****************************************************************/

    String WX_AES = "aes";

    /********************************************************微信第三方平台*****************************************************************/

    /**
     * 微信公众号登录授权入口URL
     */
    String AUTH_ENTRY_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=[COMPONENT_APPID]&pre_auth_code=[PRE_AUTH_CODE]&redirect_uri=[REDIRECT_URI]";

    /**
     * 获取第三方平台component_access_token
     */
    String COMPONENT_ACCESS_TOKEN_URI = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";

    /**
     * 获取预授权码pre_auth_code
     */
    String PRE_AUTH_CODE_URI = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=[COMPONENT_ACCESS_TOKEN]";

    /**
     * 使用授权码换取公众号或小程序的接口调用凭据和授权信息
     */
    String API_QUERY_AUTH = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=[COMPONENT_ACCESS_TOKEN]";

    /**
     * 获取authorizerAccessToken
     */
    String AUTHORIZER_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=[COMPONENT_ACCESS_TOKEN]";


    /**
     * 获取授权方的公众号帐号基本信息URL
     */
    String API_GET_AUTHORIZER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=[COMPONENT_ACCESS_TOKEN]";


    /********************************************************微信公众号*****************************************************************/

    /**
     * 获取access_token
     */
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 获取用户信息
     */
    String USER_URL_1 = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    String USER_URL_2 = " https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 二次授权
     */
    String AUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";

    /**
     * 创建菜单
     */
    String MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 查询菜单
     */
    String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    /**
     * 请求下载多媒体文件
     */
    String MEDIA_FILE_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=[ACCESS_TOKEN]&media_id=[MEDIA_ID]";

    /**
     * 客服接口-发消息 using
     */
    String SEND_KF_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=[ACCESS_TOKEN]";

    /**
     * 新增其他类型永久素材  媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）  using
     */
    String ADD_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=[ACCESS_TOKEN]&type=[TYPE]";

}
