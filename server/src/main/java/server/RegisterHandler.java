package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.http.Context;
import service.ClearService;
import service.UserService;
import dataaccess.DataAccessException;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;
import service.register.RegisterRequest;
import service.register.RegisterResult;

public class RegisterHandler {
    UserService service;

    public RegisterHandler(UserService service){
        this.service = service;
    }

    public void handle(Context context){
        String str = context.body();
        RegisterRequest request =  new Gson().fromJson(str, RegisterRequest.class);
        try {
            RegisterResult result = service.register(request);
            context.status(200);
            context.result(new Gson().toJson(result));
        } catch(DataAccessException e) {
            context.status(500);
            context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }\n");
        } catch (AlreadyTakenException e) {
            context.status(403);
            context.result("{ \"message\": \"Error: already taken\" }");
        } catch (BadRequestException e) {
            context.status(400);
            context.result("{ \"message\": \"Error: bad request\" }");
        }
    }
}
