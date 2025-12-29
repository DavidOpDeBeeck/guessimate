package app.dodb.guessimate.session.drivenadapter;

import app.dodb.smd.api.event.bus.ProcessingGroupsConfigurer;
import app.dodb.smd.spring.EnableSMD;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableSMD
@EnableJpaRepositories
@EntityScan
public class GuessimateDrivenAdapterConfiguration {

    @Bean
    public ProcessingGroupsConfigurer sessionViewProcessingGroup() {
        return spec -> spec
            .processingGroup("session_view").sync();
    }
}
