package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import chess.model.AuthData;
import chess.model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;
import register.RegisterRequest;
import register.RegisterResult;
import java.util.UUID;
import login.LoginRequest;
import login.LoginResult;
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

        String securePassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        data.createUser(new UserData(request.username(), securePassword, request.email()));
        String authToken = generateToken();
        data.createAuth(new AuthData(authToken, request.username()));

        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, UnauthorizedException, BadRequestException {
        if(request.username() == null || request.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = data.getUser(request.username());
        if(user == null || ! BCrypt.checkpw(request.password(), user.password())){
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
