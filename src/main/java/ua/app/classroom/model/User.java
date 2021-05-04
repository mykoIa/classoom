package ua.app.classroom.model;

import java.io.Serializable;

public class User implements Serializable {

    private String fullName;
    private boolean handUp;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isHandUp() {
        return handUp;
    }

    public void setHandUp(boolean handUp) {
        this.handUp = handUp;
    }

}
