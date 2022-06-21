package project.kiyobackend.store.domain.domain.store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Store {

    // TODO : GeneratedValue 속성 다시 정확히 알아보기
    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name")
    private String name;

    @Embedded
    private Address address;

    @Column(name = "call_number")
    private String call;

    @Embedded
    private Comment comment;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "image",
            joinColumns = @JoinColumn(name = "store_id"))
    @OrderColumn(name = "image_idx")
    private List<Image> images = new ArrayList<>();

    private int bookmarkCount;


    @Column(name = "open_time")
    private String time;

    private boolean isKids;

    @Builder
    public Store(String name, Address address, String call, Comment comment, List<Image> images, int bookmarkCount, String time, boolean isKids) {
        this.name = name;
        this.address = address;
        this.call = call;
        this.comment = comment;
        this.images = images;
        this.bookmarkCount = bookmarkCount;
        this.time = time;
        this.isKids = isKids;
    }

    // 도메인 로직을 잘 설정해보자
}
