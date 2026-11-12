package ui;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import com.google.gson.Gson;
import model.*;
import client.ResponseException;
import client.ServerFacade;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

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
}
