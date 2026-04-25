package app.dodb.guessimate.lobby.drivingadapter;

import app.dodb.guessimate.lobby.api.command.WebSocketCommand;
import app.dodb.guessimate.lobby.api.event.WebSocketEvent;
import app.dodb.smd.api.event.bus.ProcessingGroupsConfigurer;
import app.dodb.smd.spring.EnableSMD;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@ComponentScan
@EnableScheduling
@EnableSMD
public class GuessimateLobbyDrivingAdapterConfiguration {

    @Bean
    public ProcessingGroupsConfigurer drivingAdapterProcessingGroups() {
        return spec -> spec
            .processingGroup("estimation_timer_process_manager").sync();
    }

    @Bean
    public ObjectMapper webSocketObjectMapper() {
        return JsonMapper.builder()
            .addMixIn(WebSocketCommand.class, WebSocketCommandMixin.class)
            .addMixIn(WebSocketEvent.class, WebSocketEventMixin.class)
            .build();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetUsernameCommand.class, name = "SetUsernameCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetEstimateCommand.class, name = "SetEstimateCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.ClearEstimateCommand.class, name = "ClearEstimateCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.StartEstimationCommand.class, name = "StartEstimationCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.CompleteEstimationCommand.class, name = "CompleteEstimationCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetUserRoleCommand.class, name = "SetUserRoleCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetDeckCommand.class, name = "SetDeckCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetAutoRevealCommand.class, name = "SetAutoRevealCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetTimerDurationCommand.class, name = "SetTimerDurationCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetReactionsEnabledCommand.class, name = "SetReactionsEnabledCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetReactionCommand.class, name = "SetReactionCommand"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.command.SetAutoJoinCommand.class, name = "SetAutoJoinCommand")
    })
    private interface WebSocketCommandMixin {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.EstimateSetEvent.class, name = "EstimateSetEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.EstimateClearedEvent.class, name = "EstimateClearedEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.EstimationCompletedEvent.class, name = "EstimationCompletedEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.EstimationStartedEvent.class, name = "EstimationStartedEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.KeepAliveEvent.class, name = "KeepAliveEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.LobbyInfoSetEvent.class, name = "LobbyInfoSetEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.UserConnectedEvent.class, name = "UserConnectedEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.UserDisconnectedEvent.class, name = "UserDisconnectedEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.UsernameSetEvent.class, name = "UsernameSetEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.UserRoleSetEvent.class, name = "UserRoleSetEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.DeckSetEvent.class, name = "DeckSetEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.AutoRevealEnabledEvent.class, name = "AutoRevealEnabledEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.AutoRevealDisabledEvent.class, name = "AutoRevealDisabledEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.TimerDurationSetEvent.class, name = "TimerDurationSetEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.ReactionsDisabledEvent.class, name = "ReactionsDisabledEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.ReactionsEnabledEvent.class, name = "ReactionsEnabledEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.ReactionSetEvent.class, name = "ReactionSetEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.ReactionClearedEvent.class, name = "ReactionClearedEvent"),
        @JsonSubTypes.Type(value = app.dodb.guessimate.lobby.api.event.AutoJoinUpdatedEvent.class, name = "AutoJoinUpdatedEvent")
    })
    private interface WebSocketEventMixin {
    }

}
