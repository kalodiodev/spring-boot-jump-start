package eu.kalodiodev.springjumpstart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Audit Configuration
 * <p>Enabling jpa audit</p>
 * 
 * @author Athanasios Raptodimos
 */
@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

}
