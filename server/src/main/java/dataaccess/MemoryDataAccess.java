package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.UserData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
    private final HashMap<String, UserData> userList = new HashMap<>();
    private final HashMap<String, AuthData> authList = new HashMap<>();
    private final HashMap<Integer, GameData> gameList = new HashMap<>();
    private int nextGameID = 1;

    public void clear(){
        userList.clear();
        authList.clear();
        gameList.clear();
        nextGameID = 1;
    }

    public UserData getUser(String username) {
        return userList.get(username);
    }
    public void createUser(UserData data) {
        userList.put(data.username(), data);
    }
    public void createAuth(AuthData data) {
        authList.put(data.authToken(), data);
    }

    public AuthData getAuth(String token){
        if(authList.get(token) == null){
            return null;
        }
        return authList.get(token);
    }
    public void deleteAuth(String token){
        authList.remove(token);
    }

    public int createGame(String name) {
        int gameID = nextGameID;
        nextGameID = nextGameID + 1;
        GameData game = new GameData(gameID, null, null, name, new ChessGame());
        gameList.put(gameID, game);
        return gameID;
    }

    public Collection<GameData> listGames() {
        return gameList.values();
    }

    public GameData getGame(int gameID){
        return gameList.get(gameID);
    }

    public void updateGame(GameData game) {
        gameList.put(game.gameID(), game);
    }
}
