package ui;

import com.google.gson.Gson;
import model.*;
import client.ResponseException;
import client.ServerFacade;
import register.RegisterResult;
import service.listgames.ListGamesResult;
import service.login.LoginResult;
import java.util.*;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken = null;
    private List<GameData> gameList = new ArrayList<>();

    public ChessClient(int port) throws ResponseException{
        server = new ServerFacade(port);
    }

    public void run(){
        System.out.println("Welcome to 240 Chess! Type help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            System.out.print("\n[" + state + "] >>> ");
            String nextLine = scanner.nextLine();
            result = eval(nextLine);
            if(!result.equals("quit")){
                System.out.print(result);
            }
        }
        System.out.println("Bye bye!");
    }

    public String eval(String input){
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = tokens[0].toLowerCase();
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd){
            case "quit" -> "quit";
            case "login" -> login(params);
            case "register" -> register(params);
            case "logout" -> logout();
            case "create" -> create(params);
            case "list" -> list();
            default -> help();
        };
    }

    public String help() {
        if(state == State.SIGNEDOUT){
            return """
                   register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                   login <USERNAME> <PASSWORD> - to play chess
                   quit - playing chess
                   help - with possible commands
                   """;
        } else {
            return """
                   create <NAME> - a game
                   list - games
                   join <ID> [WHITE|BLACK] - a game
                   observe <ID> a game
                   logout - when you are done
                   quit - playing chess
                   help - with possible commands
                   """;
        }
    }

    private String register(String[] params){
        if(state != State.SIGNEDOUT){
            return "Error: You're already logged in!\n";
        }
        if(params.length != 3){
            return "Error: Register takes 3 arguments (register <USERNAME> <PASSWORD> <EMAIL>).\n";
        }
        try{
            RegisterResult registerResult = server.register(params[0], params[1], params[2]);
            authToken = registerResult.authToken();
            state = State.SIGNEDIN;
            return "Registered and logged in as " + registerResult.username() + ".\n";
        } catch (ResponseException e){
            return "Registration failed: " + e.getMessage() + "\n";
        }
    }

    private String login(String[] params){
        if(state != State.SIGNEDOUT){
            return "Error: You're already logged in!\n";
        }
        if(params.length != 2){
            return "Error: Login takes 2 arguments (login <USERNAME> <PASSWORD>).\n";
        }
        try{
            LoginResult loginResult = server.login(params[0], params[1]);
            authToken = loginResult.authToken();
            state = State.SIGNEDIN;
            return "Logged in as " + loginResult.username() + ".\n";
        } catch (ResponseException e){
            return "Login failed: " + e.getMessage() + "\n";
        }
    }

    private String logout(){
        if(state == State.SIGNEDOUT){
            return "Error: You're not logged in!\n";
        }
        try{
            server.logout(authToken);
            authToken = null;
            state = State.SIGNEDOUT;
            return "Logged out.\n";
        } catch (ResponseException e){
            return "Logout failed: " + e.getMessage() + "\n";
        }
    }

    private String create(String[] params){
        if(state == State.SIGNEDOUT){
            return "Error: You're not logged in!\n";
        }
        try{
            server.createGame(authToken, params[0]);
            return "Created game '" + params[0] + "'.\n";
        } catch (ResponseException e){
            return "Create game failed: " + e.getMessage() + "\n";
        }
    }

    private String list(){
        if(state == State.SIGNEDOUT){
            return "Error: You're not logged in!\n";
        }
        try{
            ListGamesResult listGamesResult = server.listGames(authToken);
            gameList = new ArrayList<>(listGamesResult.games());
            if(gameList.isEmpty()){
                return "No games created yet.";
            } else {
                String str = "";
                for(int i=0; i<gameList.size(); i++){
                    int num = i+1;
                    str += "" + num + ". " + gameList.get(i).gameName()
                            + "   White: " + gameList.get(i).whiteUsername() + "   Black: " + gameList.get(i).blackUsername() + "\n";
                }
                return str;
            }
        } catch (ResponseException e){
            return "List games failed: " + e.getMessage() + "\n";
        }
    }
}
