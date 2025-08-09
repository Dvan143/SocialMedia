package org.example.socialmedia.classes.brockers;

import java.io.Serial;
import java.io.Serializable;

public class MqDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
