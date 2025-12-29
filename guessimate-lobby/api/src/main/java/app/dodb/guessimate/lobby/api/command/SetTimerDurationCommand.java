package app.dodb.guessimate.lobby.api.command;

import app.dodb.guessimate.lobby.api.event.TimerDuration;

public record SetTimerDurationCommand(TimerDuration timerDuration) implements WebSocketCommand {
}

