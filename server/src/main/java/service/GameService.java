package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import service.createGame.CreateGameRequest;
import service.createGame.CreateGameResult;
import service.register.BadRequestException;

import java.util.Collection;

import service.login.UnauthorizedException;
import service.listGames.*;

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

    public ListGamesResult listGames(String authToken) throws UnauthorizedException, DataAccessException{
        System.out.println(authToken);
        authService.authenticate(authToken);
        Collection<GameData> games = data.listGames();
        return new ListGamesResult(games);
    }
}
