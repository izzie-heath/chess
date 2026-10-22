package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
    private final HashMap<String, UserData> userList = new HashMap<>();
    private final HashMap<String, AuthData> authList = new HashMap<>();
    private final HashMap<Integer, AuthData> gameList = new HashMap<>();

    public void clear(){
        userList.clear();
        authList.clear();
        gameList.clear();
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
}
