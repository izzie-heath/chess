package server;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        DataAccess data = new MemoryDataAccess();
        ClearService clearService = new ClearService(data);
        ClearHandler clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::handle);

        UserService userService = new UserService(data);
        RegisterHandler registerHandler = new RegisterHandler(userService);
        javalin.post("/user", registerHandler::handle);

        LoginHandler loginHandler = new LoginHandler(userService);
        javalin.post("/session", loginHandler::handle);

        LogoutHandler logoutHandler = new LogoutHandler(userService);
        javalin.delete("/session", logoutHandler::handle);

        GameService gameService = new GameService(data);
        CreateGameHandler createGameHandler = new CreateGameHandler(gameService);
        javalin.post("/game", createGameHandler::handle);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
