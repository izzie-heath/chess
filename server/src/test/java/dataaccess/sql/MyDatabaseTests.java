package dataaccess.sql;
import dataaccess.DataAccessException;
import dataaccess.sql.SqlDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    public void deleteAuth() throws DataAccessException {
        data = new SqlDataAccess();
        data.clear();
        AuthData authData = new AuthData("token", "izzie");
        data.createAuth(authData);
        assertDoesNotThrow(() -> data.deleteAuth("token"));
    }
}