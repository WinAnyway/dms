package pl.com.bottega.dms.application.user;

import pl.com.bottega.dms.model.EmployeeId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class User {

    @EmbeddedId
    private EmployeeId employeeId;

    private String hashedPassword;

    private String userName;

    User(){}

    public User(EmployeeId employeeId, String username, String hashedPassword) {

        this.employeeId = employeeId;
        this.userName = username;
        this.hashedPassword = hashedPassword;

    }

    public String getUserName() {
        return userName;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }
}
