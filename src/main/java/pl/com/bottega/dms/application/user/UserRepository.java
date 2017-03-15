package pl.com.bottega.dms.application.user;

import pl.com.bottega.dms.model.EmployeeId;

public interface UserRepository {

    void put(User user);

    User findByEmployeeId(EmployeeId id);

    User findByUserName(String username);

    User findByLoginAndHashedPassword(String login, String password);
}
