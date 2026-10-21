package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.login.LoginRequest;
import service.login.UnauthorizedException;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;
import service.register.RegisterRequest;

public class myTests {
    //clear tests
    @Test
    @DisplayName("Clear Runs Successfully")
    public void clearWorks(){
        DataAccess dataAccess = new MemoryDataAccess();
        ClearService clearService = new ClearService(dataAccess);
        Assertions.assertDoesNotThrow(clearService::clear);
    }


    //register tests
    @Test
    @DisplayName("Duplicate Users Can't Be Created")
    public void registerDuplicateUsers() throws BadRequestException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(registerRequest));
    }

    @Test
    @DisplayName("Username Can't Be Null When Registering")
    public void registerWithNullUsername() throws BadRequestException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest(null, "securePassword", "izzieh@byu.edu");
        Assertions.assertThrows(BadRequestException.class, () -> userService.register(registerRequest));
    }


    //login tests
    @Test
    @DisplayName("Incorrect Password Throws Error")
    public void loginWrongPassword() throws BadRequestException, DataAccessException, UnauthorizedException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "wrongPassword");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(loginRequest));
    }

    @Test
    @DisplayName("Password Can't Be Null When Logging In")
    public void loginNullPassword() throws BadRequestException, DataAccessException, UnauthorizedException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(loginRequest));
    }
}
