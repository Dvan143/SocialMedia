package org.example.socialmedia.classes.brockers;

public class MqDto {
    private final String to;
    private final String secretCode;

    public MqDto(String to, String secretCode) {
        this.to = to;
        this.secretCode = secretCode;
    }

    public String getTo() {
        return to;
    }

    public String getSecretCode() {
        return secretCode;
    }
}
