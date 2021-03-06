package project.kiyobackend.store.domain.domain.store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.kiyobackend.bookmark.domain.BookMark;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.category.domain.CategoryId;
import project.kiyobackend.convenience.domain.ConvenienceId;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.user.domain.User;
import project.kiyobackend.util.jpa.JpaBaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "store")
public class Store extends JpaBaseEntity {

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
    private List<Long> categoryIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "store_convenience",
            joinColumns = @JoinColumn(name = "store_id"))
    private List<Long> convenienceIds = new ArrayList<>();

    @Column(name = "store_name")
    private String name;

//    @Embedded
 //   private Address address;

    @Column(name = "call_number")
    private String call;

    /*
    컬렉션으로 넣을 필요 없이, 상점 하나당 simpleComment, detailComment 하나만 있으면 되므로 바로 store 테이블에 넣음
     */
    @Embedded
    private Comment comment;


    @OneToMany(mappedBy = "store" ,fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<StoreImage> storeImages = new ArrayList<>();

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
    private List<BookMark> bookMarks = new ArrayList<>();

    private int bookmarkCount;

    private int reviewCount;

    @Column(name = "open_time")
    private String time;

    private boolean isKids;

    private boolean isBooked  = false;

    private boolean isAssigned;
    /**
     * 생성 메서드, 연관관계 편의 메서드 고려!
     */

    public void setMenus(List<Menu> menus)
    {
        for (Menu menu : menus) {
            this.menus.add(menu);
        }
    }

    public void setStoreImages(List<String> storeImages)
    {
        for (String path : storeImages) {
            StoreImage storeImage = new StoreImage(path);
            this.storeImages.add(storeImage);
            storeImage.setStore(this);

        }
    }

    public void setIsBooked(boolean check)
    {
        this.isBooked = check;
    }

    public Store(String name,  String call, Comment comment, String time, boolean isKids,List<Long> categoryIds, List<Long> convenienceIds) {
        this.name = name; // 가게 이름
        this.call = call; // 가게 전화번호 주소는 잠시 삭제
        this.comment =
                comment;// 값 타입 생성자에서 생성
        this.bookmarkCount = 0;
        this.reviewCount = 0;
        this.time = time; // 영업 시간
        this.isKids = isKids; // 키즈존 여부
        this.isAssigned = false;
        categoryIds.forEach(c->this.getCategoryIds().add(c));
        convenienceIds.forEach(cv->this.getConvenienceIds().add(cv));
    }

    public static Store createStore(String name, String call, Comment comment, String time, boolean isKids,  List<Long> categoryIds, List<Long> convenienceIds, List<Menu> menus, List<String> storeImages)
    {
        Store store = new Store(name,call,comment,time,isKids,categoryIds, convenienceIds);
        store.setMenus(menus);
        store.setStoreImages(storeImages);
        return store;
    }

    public void addBookmarkCount()
    {
        this.bookmarkCount+=1;
    }

    public void minusBookmarkCount()
    {
        if(bookmarkCount < 1)
        {
            throw new IllegalArgumentException("북마크 개수가 0보다 작습니다.");
        }
        this.bookmarkCount -= 1;
    }


}
