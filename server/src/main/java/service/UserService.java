package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;
import service.register.RegisterRequest;
import service.register.RegisterResult;
import java.util.UUID;

public class UserService {
    DataAccess data;

    public UserService(DataAccess data){
        this.data = data;
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
}
