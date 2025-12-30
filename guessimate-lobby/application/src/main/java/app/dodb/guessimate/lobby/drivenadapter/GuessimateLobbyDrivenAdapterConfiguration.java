package app.dodb.guessimate.lobby.drivenadapter;

import app.dodb.smd.api.event.bus.ProcessingGroupsConfigurer;
import app.dodb.smd.spring.EnableSMD;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Configuration
@ComponentScan
@EnableSMD
@EntityScan
@EnableJpaRepositories
public class GuessimateLobbyDrivenAdapterConfiguration {

    @Bean
    public ProcessingGroupsConfigurer drivenAdapterProcessingGroups() {
        return spec -> spec
            .processingGroup("lobby_view").sync()
            .processingGroup("lobby_metrics_view").sync()
            .processingGroup("web_socket_sender").async().fireAndForget(newSingleThreadExecutor());
    }
}
