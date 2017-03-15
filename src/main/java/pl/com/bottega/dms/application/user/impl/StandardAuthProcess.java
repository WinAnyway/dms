package pl.com.bottega.dms.application.user.impl;

import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.dms.application.user.*;
import pl.com.bottega.dms.model.EmployeeId;

@Transactional
public class StandardAuthProcess implements AuthProcess {

    private UserRepository userRepository;
    private CurrentUser currentUser;

    public StandardAuthProcess(UserRepository userRepository, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @Override
    public AuthResult createAccount(CreateAccountCommand cmd) {

        if (userRepository.findByEmployeeId(new EmployeeId(cmd.getEmployeeId())) != null) {
            String message = String.format("Id: %d has been taken", cmd.getEmployeeId());
            return new AuthResult(false, message);
        }

        if (userRepository.findByUserName(cmd.getUserName()) != null) {
            String message = String.format("Username: %s has been taken", cmd.getUserName());
            return new AuthResult(false, message);
        }

        User user = new User(new EmployeeId(cmd.getEmployeeId()), cmd.getUserName(), cmd.getPassword());
        userRepository.put(user);
        return AuthResult.succes();
    }

    @Override
    public AuthResult login(LoginCommand cmd) {
        User user = userRepository.findByLoginAndHashedPassword(cmd.getLogin(), cmd.getPassword());
        if (user == null)
            return new AuthResult(false, "Login or password are not correct");
        else {
            currentUser.setEmployeeId(user.getEmployeeId());
            return AuthResult.succes();
        }
    }

}
