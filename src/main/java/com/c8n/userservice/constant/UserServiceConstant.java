package com.c8n.userservice.constant;

public class UserServiceConstant {
    public static final String AUTH_ISSUER = "com.c8n";

    public static final long MONTH_MILIS = 1000L*60*60*24*30;

    // URL constants
    public static final String API_VERSION = "/api/v1";
    public static final String URL_USER = "/user";
    public static final String URL_SAVE = "/save";
    public static final String URL_DELETE = "/delete";
    public static final String URL_LOGIN = "/login";
    public static final String URL_AUTH = "/authority";
    public static final String URL_INFO = "/getinfo";
    public static final String URL_LOGOUT = "/logout";
    public static final String URL_PING = "/ping";

    // SQL constatns
    public static final String TABLE_USER_RECORD = "user_record";
    public static final String TABLE_USER_AUTHORITY = "user_authority";
    public static final String TABLE_USER_ACCESS_LOG = "user_access_log";

    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_NAME = "name";
    public static final String COL_SURNAME = "surname";
    public static final String COL_IDENTIFIER = "identifier";
    public static final String COL_PASSWORD = "password";
    public static final String COL_CREATEDATE = "createdate";
    public static final String COL_UPDATEDATE = "updatedate";
    public static final String COL_DELETED = "deleted";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_ROLE_ID = "user_role_id";
    public static final String COL_AUTHORITY_LIST = "user_authority_list";
    public static final String COL_IP_ADDRESS = "ip_address";
    public static final String COL_URL = "url";
    public static final String COL_DETAILS = "details";


    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_USER = "authUser";

}
