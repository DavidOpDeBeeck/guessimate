package app.dodb.guessimate.session.application;

import app.dodb.guessimate.session.drivenadapter.GuessimateDrivenAdapterConfiguration;
import app.dodb.guessimate.session.drivingadapter.GuessimateDrivingAdapterConfiguration;
import app.dodb.guessimate.session.usecase.GuessimateUseCaseConfiguration;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@Import({
    GuessimateDrivingAdapterConfiguration.class,
    GuessimateUseCaseConfiguration.class,
    GuessimateDrivenAdapterConfiguration.class,
})
public class GuessimateSessionConfiguration {

//    @Bean(name = "transactionManager")
//    @ConditionalOnMissingBean(JpaTransactionManager.class)
//    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
}
