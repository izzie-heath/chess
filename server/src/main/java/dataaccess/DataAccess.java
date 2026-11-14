package dataaccess;

import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;

import java.util.Collection;

public interface DataAccess {
    void clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData data) throws DataAccessException;
    void createAuth(AuthData data) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
}
