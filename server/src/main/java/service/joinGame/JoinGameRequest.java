package service.joinGame;

import model.GameData;

public record JoinGameRequest(Integer gameID, String playerColor) {
}
