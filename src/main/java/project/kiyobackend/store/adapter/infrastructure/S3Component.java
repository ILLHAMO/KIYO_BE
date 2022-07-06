package project.kiyobackend.store.adapter.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3Component {

    private String bucket;
}
