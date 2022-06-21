package project.kiyobackend.store.domain.domain.store;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Address {

    private String address;

    private String addressMap;

    private String bigCity;

    private String smallCity;

}
