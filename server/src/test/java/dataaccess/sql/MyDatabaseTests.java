package dataaccess.sql;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.sql.SqlDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MyDatabaseTests {
    private SqlDataAccess data;

    //clear
    @Test
    @DisplayName("Clear throws no errors")
    public void clearWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        assertDoesNotThrow(() -> data.clear());
    }

    //getUser tests
    @Test
    @DisplayName("getUser throws no errors")
    public void getUserWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        UserData userData = new UserData("izzie", "password", "izzieh@byu.edu");
        data.createUser(userData);
        assertDoesNotThrow(() -> data.getUser("izzie"));
    }

    //createUser tests
    @Test
    @DisplayName("createUser throws no errors")
    public void createUserWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        UserData userData = new UserData("izzie", "password", "izzieh@byu.edu");
        assertDoesNotThrow(() -> data.createUser(userData));
    }
    @Test
    @DisplayName("Null data throws error when creating user")
    public void createUserNullData() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        UserData userData = new UserData("izzie", "password", null);
        assertThrows(DataAccessException.class, () -> data.createUser(userData));
    }

    //createAuth tests
    @Test
    @DisplayName("createAuth throws no errors")
    public void createAuthWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        AuthData authData = new AuthData("token", "izzie");
        assertDoesNotThrow(() -> data.createAuth(authData));
    }
    @Test
    @DisplayName("createAuth throws error when given duplicate auth token")
    public void createAuthDuplicateToken() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        AuthData authData = new AuthData("token", "izzie");
        AuthData authData2 = new AuthData("token", "ozzy");
        data.createAuth(authData);
        assertThrows(DataAccessException.class, () -> data.createAuth(authData2));
    }

    //getAuth tests
    @Test
    @DisplayName("getAuth throws no errors")
    public void getAuthWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        AuthData authData = new AuthData("token", "izzie");
        data.createAuth(authData);
        assertDoesNotThrow(() -> data.getAuth("token"));
    }

    //deleteAuth tests
    @Test
    @DisplayName("deleteAuth throws no errors")
    public void deleteAuthWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        AuthData authData = new AuthData("token", "izzie");
        data.createAuth(authData);
        assertDoesNotThrow(() -> data.deleteAuth("token"));
    }

    //createGame tests
    @Test
    @DisplayName("createGame throws no errors")
    public void createGameWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "izzie", "ozzy", "coolGame", game);
        assertDoesNotThrow(() -> data.createGame("coolGame"));
    }
    @Test
    @DisplayName("createGame with null gameName throws error")
    public void createGameNullGameName() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        assertThrows(DataAccessException.class, () -> data.createGame(null));
    }

    //listGames tests
    @Test
    @DisplayName("listGames throws no errors")
    public void listGamesWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "izzie", "ozzy", "coolGame", game);
        GameData gameData2 = new GameData(2, "izzie", "ozzy", "coolerGame", game);
        data.createGame("coolGame");
        data.createGame("coolerGame");
        assertDoesNotThrow(() -> data.listGames());
    }

    //getGame tests
    @Test
    @DisplayName("getGame throws no errors")
    public void getGameWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "izzie", "ozzy", "coolGame", game);
        assertDoesNotThrow(() -> data.getGame(1));
    }
    @Test
    @DisplayName("getGame returns the stored game")
    public void getGameReturnsGame() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "izzie", "ozzy", "coolGame", game);
        assertEquals("coolGame", gameData.gameName());
        assertEquals(new ChessGame(), gameData.game());
    }

    //updateGame tests
    @Test
    @DisplayName("updateGame throws no errors")
    public void updateGameWorks() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "izzie", "ozzy", "coolGame", game);
        assertDoesNotThrow(() -> data.updateGame(gameData));
    }
    @Test
    @DisplayName("updateGame with null gameData throws error")
    public void updateGameNullGameName() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        int id = data.createGame("coolGame");
        GameData badGame = new GameData(id, null, null, null, new ChessGame());
        assertThrows(DataAccessException.class, () -> data.updateGame(badGame));
    }
}