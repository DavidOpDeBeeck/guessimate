package app.dodb.guessimate.lobby.application;

import app.dodb.guessimate.lobby.drivenadapter.GuessimateLobbyDrivenAdapterConfiguration;
import app.dodb.guessimate.lobby.drivingadapter.GuessimateLobbyDrivingAdapterConfiguration;
import app.dodb.guessimate.lobby.usecase.GuessimateLobbyUseCaseConfiguration;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@Import({
    GuessimateLobbyDrivingAdapterConfiguration.class,
    GuessimateLobbyUseCaseConfiguration.class,
    GuessimateLobbyDrivenAdapterConfiguration.class
})
@ComponentScan
public class GuessimateLobbyConfiguration {

//    @Bean(name = "transactionManager")
//    @ConditionalOnMissingBean(JpaTransactionManager.class)
//    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
}
