package client;

import org.junit.jupiter.api.*;
import register.RegisterResult;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearAll() throws ResponseException {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    //register tests
    @Test
    @DisplayName("Register returns auth token")
    public void registerReturnsAuth() throws Exception {
        RegisterResult registerResult = serverFacade.register("izzie", "coolpassword", "izzieh@byu.edu");
        assertNotNull(registerResult.authToken());
        assertTrue(registerResult.authToken().length() > 10);
    }
}
