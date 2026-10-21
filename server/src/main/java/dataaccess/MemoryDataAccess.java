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
}
