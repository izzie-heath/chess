package service.listGames;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
}
