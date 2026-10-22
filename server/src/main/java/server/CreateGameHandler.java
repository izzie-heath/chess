package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.GameService;
import dataaccess.DataAccessException;
import service.createGame.CreateGameRequest;
import service.createGame.CreateGameResult;
import service.login.UnauthorizedException;
import service.register.BadRequestException;

public class CreateGameHandler {
    GameService service;

    public CreateGameHandler(GameService service){
        this.service = service;
    }

    public void handle(Context context){
        String header = context.header("authorization");
        CreateGameRequest request = new Gson().fromJson(context.body(), CreateGameRequest.class);
        try {
            CreateGameResult result = service.createGame(header, request);
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
