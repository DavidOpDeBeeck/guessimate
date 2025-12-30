package app.dodb.guessimate.lobby.usecase;

import app.dodb.smd.api.event.bus.ProcessingGroupsConfigurer;
import app.dodb.smd.spring.EnableSMD;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableSMD
public class GuessimateLobbyUseCaseConfiguration {

    @Bean
    public ProcessingGroupsConfigurer useCaseProcessingGroups() {
        return spec -> spec
            .processingGroup("lobby_creation").sync();
    }
}
