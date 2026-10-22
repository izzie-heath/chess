package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.UserService;
import dataaccess.DataAccessException;
import service.login.LoginRequest;
import service.login.UnauthorizedException;
import service.register.BadRequestException;
import service.login.LoginResult;

public class LogoutHandler {
    UserService service;

    public LogoutHandler(UserService service){
        this.service = service;
    }

    public void handle(Context context){
        String header = context.header("authorization");
        try {
            service.logout(header);
            context.status(200);
            context.result("{}");
        } catch(DataAccessException e) {
            context.status(500);
            context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }\n");
        } catch(UnauthorizedException e) {
            context.status(401);
            context.result("{ \"message\": \"Error: unauthorized\" }");
        }
    }

}
