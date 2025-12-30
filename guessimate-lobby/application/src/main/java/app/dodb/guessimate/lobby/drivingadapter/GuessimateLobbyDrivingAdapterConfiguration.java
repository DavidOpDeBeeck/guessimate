package app.dodb.guessimate.lobby.drivingadapter;

import app.dodb.guessimate.lobby.api.command.WebSocketCommand;
import app.dodb.guessimate.lobby.api.event.WebSocketEvent;
import app.dodb.smd.api.event.bus.ProcessingGroupsConfigurer;
import app.dodb.smd.spring.EnableSMD;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator.Validity.ALLOWED;
import static com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator.Validity.DENIED;

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
        ObjectMapper mapper = new ObjectMapper();

        mapper.setDefaultTyping(new WebSocketTypeResolverBuilder());
        for (Class<?> commandClass : WebSocketCommand.class.getPermittedSubclasses()) {
            mapper.registerSubtypes(new NamedType(commandClass, commandClass.getSimpleName()));
        }

        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    private static class WebSocketTypeResolverBuilder extends DefaultTypeResolverBuilder {

        public WebSocketTypeResolverBuilder() {
            super(NON_FINAL, new WebSocketPolymorphicTypeValidator());
            init(JsonTypeInfo.Id.SIMPLE_NAME, null);
            inclusion(JsonTypeInfo.As.PROPERTY);
            typeProperty("type");
        }

        @Override
        public boolean useForType(JavaType t) {
            Class<?> rawClass = t.getRawClass();
            return WebSocketEvent.class.isAssignableFrom(rawClass) || WebSocketCommand.class.isAssignableFrom(rawClass);
        }
    }

    private static class WebSocketPolymorphicTypeValidator extends PolymorphicTypeValidator.Base {

        @Override
        public Validity validateBaseType(MapperConfig<?> config, JavaType baseType) {
            Class<?> rawClass = baseType.getRawClass();
            return WebSocketEvent.class.isAssignableFrom(rawClass) || WebSocketCommand.class.isAssignableFrom(rawClass)
                ? ALLOWED
                : DENIED;
        }
    }

}
