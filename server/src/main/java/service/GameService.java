package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.creategame.CreateGameRequest;
import service.creategame.CreateGameResult;
import service.joingame.JoinGameRequest;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;

import java.util.Collection;

import service.login.UnauthorizedException;
import service.listgames.*;

public class GameService {
    DataAccess data;
    AuthService authService;

    public GameService(DataAccess data){
        this.data = data;
        this.authService = new AuthService(data);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request)
            throws BadRequestException, UnauthorizedException, DataAccessException {
        if(request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        authService.authenticate(authToken);
        int gameNum = data.createGame(request.gameName());
        return new CreateGameResult(gameNum);
    }

    public ListGamesResult listGames(String authToken)
            throws UnauthorizedException, DataAccessException{
        System.out.println(authToken);
        authService.authenticate(authToken);
        Collection<GameData> games = data.listGames();
        return new ListGamesResult(games);
    }

    public void joinGame(String authToken, JoinGameRequest request)
            throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        if(request.gameID() == null || request.playerColor() == null){
            throw new BadRequestException("Error: bad request");
        }
        if(!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK")){
            throw new BadRequestException("Error: bad request");
        }
        AuthData authData = authService.authenticate(authToken);
        GameData game = data.getGame(request.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData updatedGame;
        if(request.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
        } else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), authData.username(), game.gameName(), game.game());
        }
        data.updateGame(updatedGame);
    }

}
