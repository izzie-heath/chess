package server;

import dataaccess.DataAccess;
import io.javalin.http.Context;
import service.ClearService;
import dataaccess.DataAccessException;

public class ClearHandler {
    ClearService service;

    public ClearHandler(ClearService service){
        this.service = service;
    }

    public void handle(Context context){
        try {
            service.clear();
            context.status(200);
            context.result("{}");
        } catch(DataAccessException e) {
            context.status(500);
            context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }\n");
        }
    }
}
