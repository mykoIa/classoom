package ua.app.classroom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user_account")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "password")
    private String password;
    @Column(name = "d", nullable = false)
    private boolean handUp;
    @Column(name = "online", nullable = false)
    private boolean userAlreadySignedUp;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUserAlreadySignedUp() {
        return userAlreadySignedUp;
    }

    public void setUserAlreadySignedUp(boolean userAlreadySignedUp) {
        this.userAlreadySignedUp = userAlreadySignedUp;
    }
}
