package pl.com.bottega.dms.aceptance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.dms.application.user.*;
import pl.com.bottega.dms.model.EmployeeId;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AuthProcessTest {

    @Autowired
    private AuthProcess authProcess;

    @Autowired
    private CurrentUser currentUser;

    @Test
    public void shouldCreateUserIfNotExist() {
        CreateAccountCommand cmd = new CreateAccountCommand();
        cmd.setEmployeeId(1L);
        cmd.setUserName("kuba");
        cmd.setPassword("xxx");

        AuthResult result = authProcess.createAccount(cmd);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldNotCreateUserIfEmployeeIdExist() {
        CreateAccountCommand cmd = new CreateAccountCommand();
        cmd.setEmployeeId(1L);
        cmd.setUserName("kuba");
        cmd.setPassword("xxx");
        AuthResult result = authProcess.createAccount(cmd);

        AuthResult result2 = authProcess.createAccount(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result2.isSuccess()).isFalse();
        assertThat(result2.getErrorMessage()).isEqualTo("Id: 1 has been taken");

    }

    @Test
    public void shouldNotCreateUserIfUserNameExist() {
        CreateAccountCommand cmd = new CreateAccountCommand();
        cmd.setEmployeeId(1L);
        cmd.setUserName("kuba");
        cmd.setPassword("xxx");

        CreateAccountCommand cmd2 = new CreateAccountCommand();
        cmd2.setEmployeeId(2L);
        cmd2.setUserName("kuba");
        cmd2.setPassword("xxx");


        AuthResult result = authProcess.createAccount(cmd);
        AuthResult result2 = authProcess.createAccount(cmd2);

        assertThat(result.isSuccess()).isTrue();

        assertThat(result2.isSuccess()).isFalse();
        assertThat(result2.getErrorMessage()).isEqualTo("Username: kuba has been taken");
    }

    @Test
    public void shouldLoginIfCorrectUserNameAndPassword() {
        CreateAccountCommand cmd = createAccountCommand();
        authProcess.createAccount(cmd);

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setLogin("kuba");
        loginCommand.setPassword("xxx");
        AuthResult result = authProcess.login(loginCommand);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldLoginIfCorrectEmployeeIdAndPassword() {
        CreateAccountCommand cmd = createAccountCommand();
        authProcess.createAccount(cmd);

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setLogin("1");
        loginCommand.setPassword("xxx");
        AuthResult result = authProcess.login(loginCommand);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    public void shouldNotLogInIfFalseUsernameAndCorrectPassword() {
        CreateAccountCommand cmd = createAccountCommand();
        authProcess.createAccount(cmd);

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setLogin("janek");
        loginCommand.setPassword("xxx");
        AuthResult result = authProcess.login(loginCommand);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Login or password are not correct");
    }

    @Test
    public void shouldNotLogInIfFalseEmployeeIdAndCorrectPassword() {
        CreateAccountCommand cmd = createAccountCommand();
        authProcess.createAccount(cmd);

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setLogin("2");
        loginCommand.setPassword("xxx");
        AuthResult result = authProcess.login(loginCommand);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Login or password are not correct");
    }

    @Test
    public void shouldNotLogInIfCorrectLoginAndFalsePassword() {
        CreateAccountCommand cmd = createAccountCommand();
        authProcess.createAccount(cmd);

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setLogin("1");
        loginCommand.setPassword("yyy");
        AuthResult result = authProcess.login(loginCommand);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("Login or password are not correct");
    }

    @Test
    public void shouldRememberCurrentEmployeeId() {
        CreateAccountCommand cmd = createAccountCommand();
        authProcess.createAccount(cmd);

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setLogin("kuba");
        loginCommand.setPassword("xxx");
        authProcess.login(loginCommand);

        assertThat(currentUser.getEmployeeId()).isEqualTo(new EmployeeId(1L));
    }

    private CreateAccountCommand createAccountCommand() {
        CreateAccountCommand cmd = new CreateAccountCommand();
        cmd.setEmployeeId(1L);
        cmd.setUserName("kuba");
        cmd.setPassword("xxx");
        return cmd;
    }



}
