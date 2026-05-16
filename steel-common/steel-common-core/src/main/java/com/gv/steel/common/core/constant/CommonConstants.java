package com.gv.steel.common.core.constant;

public interface CommonConstants {
    /**
     * 超级管理员角色ID
     */
    long SUPER_ROLE_ID = 1;

    /**
     * mapstruct
     */
    String SPRING = "spring";

    /**
     * parent id
     */
    long TREE_ROOT_ID = 0;

    /*-------------------API--------------------*/
    int FAIL = 0;
    int SUCCESS = 1;
    /**
     * token expire
     */
    int TOKEN_EXPIRE = 4240;

    /**
     * unauthorized
     */
    int UNAUTHORIZED = 4010;
    /*-------------------API--------------------*/

    /*-------------------common-----------------*/
    int STATUS_NORMAL = 0;
    String UTF8 = "UTF-8";
    String CONTENT_TYPE = "application/json; charset=utf-8";
    int TRUE = 1;
    int FALSE = 0;
    /*-------------------common-----------------*/

    /*-------------------entity-------------------*/
    int DELETE_FLAG_Y = 1;
    int DELETE_FLAG_N = 0;
    /*-------------------entity-------------------*/

    /*-------------------security-------------------*/
    /**
     * 请求开始时间
     */
    String REQUEST_START_TIME = "REQUEST_START_TIME";
    /*-------------------security-------------------*/

    /*-------------------common package-------------------*/
    /**
     * 默认扫描的 dao 包
     */
    String DEFAULT_DAO_PACKAGE = "com.gv.steel.**.dao.**";
    /**
     * 默认扫描的 feign 包
     */
    String DEFAULT_FEIGN_PACKAGE = "com.gv.steel";
    /*-------------------common package-------------------*/

    /*-------------------page---------------------------*/
    String PAGE_NO = "pageNo";
    String PAGE_SIZE = "pageSize";
    /*-------------------page---------------------------*/

    /**
     * 语言
     */
    String HEADER_LANG = "Accept-Language";
}
