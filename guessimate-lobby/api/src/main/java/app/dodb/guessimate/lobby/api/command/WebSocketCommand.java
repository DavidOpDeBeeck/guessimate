package app.dodb.guessimate.lobby.api.command;

public sealed interface WebSocketCommand
    permits SetUsernameCommand,
    SetEstimateCommand,
    ClearEstimateCommand,
    StartEstimationCommand,
    CompleteEstimationCommand,
    SetUserRoleCommand,
    SetDeckCommand,
    SetAutoRevealCommand,
    SetTimerDurationCommand,
    SetReactionsEnabledCommand,
    SetReactionCommand,
    SetAutoJoinCommand {
}
