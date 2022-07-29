package com.wicoding.winwebradio;

import java.util.Date;

/**
 * Created by HunTerAnD1 on 05/01/2017.
 */

public class ChatMessage {
    private String MessageId;
    private String MessageLID;
    private String MessageUser;
    private String MessageText;
    private String MessageTime;

    public ChatMessage(String messageId, String messageLID, String messageUser, String messageText, String messageTime) {
        MessageId = messageId;
        MessageLID = messageLID;
        MessageUser = messageUser;
        MessageText = messageText;
        MessageTime = messageTime;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getMessageLID() {
        return MessageLID;
    }

    public void setMessageLID(String messageLID) {
        MessageLID = messageLID;
    }

    public String getMessageUser() {
        return MessageUser;
    }

    public void setMessageUser(String messageUser) {
        MessageUser = messageUser;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(String messageTime) {
        MessageTime = messageTime;
    }
}
