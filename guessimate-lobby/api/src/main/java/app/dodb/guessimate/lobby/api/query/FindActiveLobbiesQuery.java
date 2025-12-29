package app.dodb.guessimate.lobby.api.query;

import app.dodb.smd.api.query.Query;

import java.util.List;

public record FindActiveLobbiesQuery() implements Query<List<String>> {
}
