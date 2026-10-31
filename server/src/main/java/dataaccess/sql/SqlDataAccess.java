package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
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
import java.util.ArrayList;
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
                        return new UserData(resultSet.getString("username"), resultSet.getString("password"),
                                resultSet.getString("email"));
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
        String statement = "INSERT INTO gameTable (gameName, game) VALUES (?, ?)";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, gameName);
                String gson = new Gson().toJson(new ChessGame());
                preparedStatement.setString(2, gson);
                preparedStatement.executeUpdate();
                try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                    if(resultSet.next()){
                        resultSet.getInt(1);
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to create game: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    ArrayList<GameData> gameDataList = new ArrayList<>();
                    while(resultSet.next()){
                        String gameJson = resultSet.getString("game");
                        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                        gameDataList.add(new GameData(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"), resultSet.getString("gameName"), game));
                    }
                    return gameDataList;
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to list games: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable WHERE gameID = ?";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setInt(1, gameID);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()){
                        String gameJson = resultSet.getString("game");
                        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                        return new GameData(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"), resultSet.getString("gameName"), game);
                    }
                }
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to get game: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String statement = "UPDATE gameTable SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try(Connection connection = DatabaseManager.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(statement)){
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                String gson = new Gson().toJson(game.game());
                preparedStatement.setString(4, gson);
                preparedStatement.setInt(5, game.gameID());
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            throw new DataAccessException("Unable to update game: " + e.getMessage());
        }
    }
}