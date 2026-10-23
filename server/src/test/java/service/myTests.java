package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.createGame.CreateGameRequest;
import service.joinGame.JoinGameRequest;
import service.listGames.ListGamesResult;
import service.login.LoginRequest;
import service.login.LoginResult;
import service.login.UnauthorizedException;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;
import service.register.RegisterRequest;
import service.register.RegisterResult;

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
    public void loginWrongPassword() throws BadRequestException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "wrongPassword");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(loginRequest));
    }

    @Test
    @DisplayName("Password Can't Be Null When Logging In")
    public void loginNullPassword() throws BadRequestException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login(loginRequest));
    }


    //logout tests
    @Test
    @DisplayName("Logout Works Without Errors")
    public void logoutWorks() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "securePassword");
        LoginResult loginResult = userService.login(loginRequest);
        Assertions.assertDoesNotThrow(() -> userService.logout(loginResult.authToken()));
    }

    @Test
    @DisplayName("Logout Fails When No Auth Token Is Provided")
    public void logoutNoAuthToken() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(null));
    }


    //create game tests
    @Test
    @DisplayName("Create Game Works Without Errors")
    public void createGameWorks() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException{
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "securePassword");
        LoginResult loginResult = userService.login(loginRequest);
        GameService gameService = new GameService(dataAccess);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        Assertions.assertDoesNotThrow(() -> gameService.createGame(loginResult.authToken(), createGameRequest));
    }

    @Test
    @DisplayName("Create Game Fails When No Auth Token Is Provided")
    public void createGameNoAuthToken() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        GameService gameService = new GameService(dataAccess);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.createGame("", createGameRequest));
    }

    //list games test
    @Test
    @DisplayName("Create Game Works Without Errors")
    public void listGamesWorks() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException{
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "securePassword");
        LoginResult loginResult = userService.login(loginRequest);
        GameService gameService = new GameService(dataAccess);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        gameService.createGame(loginResult.authToken(), createGameRequest);
        Assertions.assertDoesNotThrow(() -> gameService.listGames(loginResult.authToken()));
    }

    @Test
    @DisplayName("Create Game Fails When No Auth Token Is Provided")
    public void listGamesNoAuthToken() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException {
        DataAccess dataAccess = new MemoryDataAccess();
        GameService gameService = new GameService(dataAccess);
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(""));
    }

    //join game
    @Test
    @DisplayName("Join Game Works With White Piece")
    public void joinGameWorksWhite() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException{
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "securePassword");
        LoginResult loginResult = userService.login(loginRequest);
        GameService gameService = new GameService(dataAccess);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        gameService.createGame(loginResult.authToken(), createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest(1, "WHITE");
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(loginResult.authToken(), joinGameRequest));
    }

    @Test
    @DisplayName("Join Game Works With Black Piece")
    public void joinGameWorksBlack() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException{
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "securePassword");
        LoginResult loginResult = userService.login(loginRequest);
        GameService gameService = new GameService(dataAccess);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        gameService.createGame(loginResult.authToken(), createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest(1, "BLACK");
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(loginResult.authToken(), joinGameRequest));
    }

    @Test
    @DisplayName("Join Game Throws Error With Invalid Piece Color")
    public void joinGameInvalidColor() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException{
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "securePassword");
        LoginResult loginResult = userService.login(loginRequest);
        GameService gameService = new GameService(dataAccess);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        gameService.createGame(loginResult.authToken(), createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest(1, "GREEN");
        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(loginResult.authToken(), joinGameRequest));
    }

    @Test
    @DisplayName("Join Game Color Already Taken")
    public void joinGameColorTaken() throws BadRequestException, UnauthorizedException, DataAccessException, AlreadyTakenException{
        DataAccess dataAccess = new MemoryDataAccess();
        UserService userService = new UserService(dataAccess);
        RegisterRequest registerRequest = new RegisterRequest("izzie", "securePassword", "izzieh@byu.edu");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("izzie", "securePassword");
        LoginResult loginResult = userService.login(loginRequest);
        GameService gameService = new GameService(dataAccess);
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        gameService.createGame(loginResult.authToken(), createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest(1, "BLACK");
        gameService.joinGame(loginResult.authToken(), joinGameRequest);
        JoinGameRequest joinGameRequest2 = new JoinGameRequest(1, "BLACK");
        Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(loginResult.authToken(), joinGameRequest2));
    }

}

