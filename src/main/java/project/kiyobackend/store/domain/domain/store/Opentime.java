package project.kiyobackend.store.domain.domain.store;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Opentime {
    private String time;
}
