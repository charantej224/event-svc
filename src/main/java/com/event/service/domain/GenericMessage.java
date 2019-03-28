package com.event.service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericMessage<T> {

    @JsonProperty("message")
    private String message;

    @JsonProperty("responseBody")
    private T messageBody;

    public GenericMessage(String message, T messageBody) {
        this.message = message;
        this.messageBody = messageBody;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(T messageBody) {
        this.messageBody = messageBody;
    }
}
