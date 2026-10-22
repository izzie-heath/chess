package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.UserService;
import dataaccess.DataAccessException;
import service.login.LoginRequest;
import service.login.UnauthorizedException;
import service.register.BadRequestException;
import service.login.LoginResult;

public class LoginHandler {
    UserService service;

    public LoginHandler(UserService service){
        this.service = service;
    }

    public void handle(Context context){
        String str = context.body();
        LoginRequest request = new Gson().fromJson(str, LoginRequest.class);
        try {
            LoginResult result = service.login(request);
            context.status(200);
            context.result(new Gson().toJson(result));
        } catch(DataAccessException e) {
            context.status(500);
            context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }\n");
        } catch (BadRequestException e) {
            context.status(400);
            context.result("{ \"message\": \"Error: bad request\" }");
        } catch (UnauthorizedException e) {
            context.status(401);
            context.result("{ \"message\": \"Error: unauthorized\" }");
        }
    }
}
