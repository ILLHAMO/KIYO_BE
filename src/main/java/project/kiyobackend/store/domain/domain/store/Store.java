package project.kiyobackend.store.domain.domain.store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.store.domain.domain.category.CategoryId;
import project.kiyobackend.store.domain.domain.convenience.ConvenienceId;
import project.kiyobackend.store.domain.domain.menu.Menu;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Store {

    // TODO : GeneratedValue 속성 다시 정확히 알아보기
    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : 나중에 조회 쿼리 짤 때 LAZY가 나을지 EAGER가 나을지 판단해보기
    /*
    프론트에서 조회할때도 1,2,3,4,5 이런 형태로 숫자 분리 로직 짤 예정
    또한 카테고리만 가져오는 쿼리 짤 때, 굳이 연관된 엔티티를 지연 로딩으로라도 가져 올 필요X
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "store_category",
            joinColumns = @JoinColumn(name = "store_id"))
    private Set<CategoryId> categoryIds;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "store_convenience",
            joinColumns = @JoinColumn(name = "store_id"))
    private Set<ConvenienceId> convenienceIds;

    @Column(name = "store_name")
    private String name;

    @Embedded
    private Address address;

    @Column(name = "call_number")
    private String call;

    /*
    컬렉션으로 넣을 필요 없이, 상점 하나당 simpleComment, detailComment 하나만 있으면 되므로 바로 store 테이블에 넣음
     */
    @Embedded
    private Comment comment;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "image",
            joinColumns = @JoinColumn(name = "store_id"))
    @OrderColumn(name = "image_idx")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Menu> menus;

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


}
