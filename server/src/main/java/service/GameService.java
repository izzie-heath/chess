package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.createGame.CreateGameRequest;
import service.createGame.CreateGameResult;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;
import service.register.RegisterRequest;
import service.register.RegisterResult;
import java.util.UUID;
import service.login.LoginRequest;
import service.login.LoginResult;
import service.login.UnauthorizedException;
import service.AuthService;

public class GameService {
    DataAccess data;
    AuthService authService;

    public GameService(DataAccess data){
        this.data = data;
        this.authService = new AuthService(data);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws BadRequestException, UnauthorizedException, DataAccessException {
        if(request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        authService.authenticate(authToken);
        int gameNum = data.createGame(request.gameName());
        return new CreateGameResult(gameNum);
    }
}
