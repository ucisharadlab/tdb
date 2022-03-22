package edu.uci.ics.perpetual.api.utilities;

public class MessageWrapper {
    private String message;

    public MessageWrapper(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
