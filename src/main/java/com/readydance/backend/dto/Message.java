package com.readydance.backend.dto;

import com.google.gson.JsonObject;

public class Message {
    private JsonObject message = new JsonObject();

    // SMS일 경우는 발신,수신 번호와 내용만
    public Message(String to, String from, String text) {
        this.message.addProperty("to", to);
        this.message.addProperty("from", from);
        this.message.addProperty("text", text);
    }


//    public void setCountry(String country) {
//        this.message.addProperty("country", country);
//    }
//
//    public void setCustomFields(JsonObject customFields) {
//        this.message.add("customFields", customFields);
//    }

    public JsonObject toJson() {
        return this.message;
    }
}
