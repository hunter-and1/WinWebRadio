package com.wicoding.winwebradio;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by HunTerAnD1 on 11/01/2017.
 */

public class Config {

    public static final String LINK_INSTGRAM = "https://www.instagram.com/winwebradio";
    public static final String LINK_FACEBOOK = "https://www.facebook.com/winwebradio";
    public static final String LINK_SITEWEB = "http://www.winwebradio.com";
    public static final String LINK_YOUTUBE = "https://www.youtube.com/channel/UCIpMeyafTEkSya0OLWOsr7g";
    public static final String LINK_PHONE = "+212528228978";

    private static final String URL_API = "http://www.winwebradio.com/mobile/api/";

    // GET - id
    // data login
    public static final String API_LOGIN = URL_API+"login/";

    // POST - uid , username
    // data new login
    public static final String API_SET_LOGIN = URL_API+"setlogin";

    // POST - lid , message
    //  data new messgae
    public static final String API_SET_MESSAGE = URL_API+"setMessage";

    // get - id
    // data messages by id
    public static final String API_GET_MESSAGES_BY_LAST = URL_API+"getMessagesbyLast/";

    // get
    // last 5 messages
    public static final String API_GET_LAST_MESSAGES = URL_API+"getLastMessage";

    public static String getUID(Context context)
    {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
