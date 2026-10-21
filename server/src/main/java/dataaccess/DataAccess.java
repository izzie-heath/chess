package dataaccess;

import model.AuthData;
import model.UserData;

public interface DataAccess {
    void clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData data) throws DataAccessException;
    void createAuth(AuthData data) throws DataAccessException;
}
