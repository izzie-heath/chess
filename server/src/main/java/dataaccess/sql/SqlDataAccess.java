package dataaccess.sql;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SqlDataAccess implements DataAccess {
    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS userTable (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS authTable (
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken)
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS gameTable (
            gameID INT NOT NULL AUTO_INCREMENT,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            game LONGTEXT NOT NULL,
            PRIMARY KEY (gameID)
        )
        """
    };

    public SqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try(Connection connection = DatabaseManager.getConnection()){
            for(String statement : createStatements){
                try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch(SQLException e) {
            throw new DataAccessException("Unable to configure database: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String[] statements = {"TRUNCATE TABLE userTable", "TRUNCATE TABLE authTable", "TRUNCATE TABLE gameTable"};
        try(Connection connection = DatabaseManager.getConnection()){
            for(String statement : statements) {
                try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Clearing the database failed: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT username, password, email FROM userTable WHERE username = ?";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()){
                        return new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
                    }
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to get user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        String statement = "INSERT INTO userTable (username, password, email) VALUES (?, ?, ?)";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, data.username());
                preparedStatement.setString(2, data.password());
                preparedStatement.setString(3, data.email());
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to create user: " + e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        String statement = "INSERT INTO authTable (authToken, username) VALUES (?, ?)";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, data.authToken());
                preparedStatement.setString(2, data.username());
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to create auth: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String statement = "SELECT authToken, username FROM authTable WHERE authToken = ?";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()){
                        return new AuthData(resultSet.getString("authToken"), resultSet.getString("username"));
                    }
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to get auth: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM authTable WHERE authToken = ?";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to delete auth: " + e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }
}