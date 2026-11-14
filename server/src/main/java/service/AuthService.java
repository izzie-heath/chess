package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import service.login.UnauthorizedException;

public class AuthService {
    DataAccess data;

    public AuthService(DataAccess data){
        this.data = data;
    }

    public AuthData authenticate(String authToken) throws DataAccessException, UnauthorizedException {
        if (data.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return data.getAuth(authToken);
    }
}
