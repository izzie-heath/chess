package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.joinGame.JoinGameRequest;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;
import service.register.RegisterRequest;
import service.register.RegisterResult;
import java.util.UUID;
import service.login.LoginRequest;
import service.login.LoginResult;
import service.login.UnauthorizedException;

public class UserService {
    DataAccess data;
    AuthService authService;

    public UserService(DataAccess data){
        this.data = data;
        this.authService = new AuthService(data);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, AlreadyTakenException, BadRequestException {
        if(request.email() == null || request.password() == null || request.username() == null){
            throw new BadRequestException("Error: bad request");
        }
        if(data.getUser(request.username()) != null){
            throw new AlreadyTakenException("Error: already taken");
        }

        data.createUser(new UserData(request.username(), request.password(), request.email()));
        String authToken = generateToken();
        data.createAuth(new AuthData(authToken, request.username()));

        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, UnauthorizedException, BadRequestException {
        if(request.username() == null || request.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        if(data.getUser(request.username()) == null || ! data.getUser(request.username()).password().equals(request.password())){
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = generateToken();
        data.createAuth(new AuthData(authToken, request.username()));
        return new LoginResult(request.username(), authToken);
    }

    public AuthData logout(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData a = authService.authenticate(authToken);
        data.deleteAuth(authToken);
        return a;
    }
}
