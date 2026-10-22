package dataaccess;

import model.AuthData;
import model.UserData;
import service.login.UnauthorizedException;

public interface DataAccess {
    void clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData data) throws DataAccessException;
    void createAuth(AuthData data) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;
}
