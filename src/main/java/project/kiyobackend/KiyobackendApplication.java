package project.kiyobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KiyobackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KiyobackendApplication.class, args);
    }

}
