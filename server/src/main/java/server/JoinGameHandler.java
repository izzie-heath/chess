package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.GameService;
import dataaccess.DataAccessException;
import service.createGame.CreateGameRequest;
import service.createGame.CreateGameResult;
import service.joinGame.JoinGameRequest;
import service.login.UnauthorizedException;
import service.register.AlreadyTakenException;
import service.register.BadRequestException;

public class JoinGameHandler {
    GameService service;

    public JoinGameHandler(GameService service){
        this.service = service;
    }

    public void handle(Context context){
        String token = context.header("authorization");
        JoinGameRequest request = new Gson().fromJson(context.body(), JoinGameRequest.class);
        try {
            service.joinGame(token, request);
            context.status(200);
            context.result(new Gson().toJson(request));
        } catch(DataAccessException e) {
            context.status(500);
            context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }\n");
        } catch (BadRequestException e) {
            context.status(400);
            context.result("{ \"message\": \"Error: bad request\" }");
        } catch (UnauthorizedException e) {
            context.status(401);
            context.result("{ \"message\": \"Error: unauthorized\" }");
        } catch (AlreadyTakenException e) {
            context.status(403);
            context.result("{ \"message\": \"Error: already taken\" }");
        }
    }
}
