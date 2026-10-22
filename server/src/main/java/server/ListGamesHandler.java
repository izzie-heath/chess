package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.GameService;
import dataaccess.DataAccessException;
import service.createGame.CreateGameRequest;
import service.createGame.CreateGameResult;
import service.listGames.ListGamesResult;
import service.login.UnauthorizedException;
import service.register.BadRequestException;

public class ListGamesHandler {
    GameService service;
    private final Gson gson = new Gson();

    public ListGamesHandler(GameService service){
        this.service = service;
    }

    public void handle(Context context){
        String header = context.header("authorization");
        try {
            ListGamesResult result = service.listGames(header);
            context.status(200);
            context.result(gson.toJson(result));
        } catch(DataAccessException e) {
            context.status(500);
            context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }\n");
        } catch(UnauthorizedException e) {
            context.status(401);
            context.result("{ \"message\": \"Error: unauthorized\" }");
        }
    }
}
