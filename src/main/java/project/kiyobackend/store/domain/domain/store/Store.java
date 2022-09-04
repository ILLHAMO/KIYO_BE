package project.kiyobackend.store.domain.domain.store;

import lombok.*;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.common.util.jpa.JpaBaseEntity;
import project.kiyobackend.store.domain.domain.tag.Tag;
import project.kiyobackend.store.domain.domain.tag.TagStore;
import project.kiyobackend.store.presentation.dto.ImageDto;
import project.kiyobackend.store.presentation.dto.TagResponseDto;
import project.kiyobackend.store.presentation.dto.menu.MenuResponseDto;
import project.kiyobackend.user.domain.User;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(name = "call_number")
    private String call;

    @Embedded
    private Comment comment;


    @OneToMany(mappedBy = "store" ,fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<StoreImage> storeImages = new ArrayList<>();

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // cascadeType.All 하면 개별 repository가 전혀 필요없다.
    // Persist는 save 필요없고, remove는 remove 필요없다.
    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "store",fetch = FetchType.LAZY,cascade = CascadeType.ALL) // 일단은 생성 주기 다름,
    private List<TagStore> tagStores = new ArrayList<>();


//    @Column(name = "open_time")
//    private String time;

    @ElementCollection
    @CollectionTable(
            name = "OPENTIME",
            joinColumns = @JoinColumn(name = "store_id")
    )
    private List<Opentime> time = new ArrayList<>();

    private boolean isKids;

    private String address;

    private String addressMap;

    private boolean isBooked  = false;

    private boolean isAssigned;

    private Long userSeq;

    public Store(String name, String call, String address, boolean isKids, List<Long> categoryIds, List<Long> convenienceIds, Long userSeq) {
        this.name = name; // 가게 이름
        this.call = call; // 가게 전화번호 주소는 잠시 삭제
        this.address = address;
      //  this.bookmarkCount = 0;
     //   this.reviewCount = 0;
        this.isKids = isKids; // 키즈존 여부
        this.isAssigned = false;
        this.userSeq = userSeq;
        categoryIds.forEach(c->this.getCategoryIds().add(c));
        convenienceIds.forEach(cv->this.getConvenienceIds().add(cv));
    }

    /**
     * 생성 메서드, 연관관계 편의 메서드 고려!
     */

    public void addBookmark(User user)
    {
        BookMark bookMark = new BookMark(user, this);
        this.bookMarks.add(bookMark);
    }

    public int getBookmarkCounts()
    {
        return bookMarks.size();
    }
    public int getReviewCounts()
    {
        return reviews.size();
    }

    public void removeBookmark(User user) {
        BookMark bookMark = new BookMark(user,this);
        this.bookMarks.remove(bookMark);

    }

    // 가게 승인 true로 변경
    public void assignStore()
    {
        this.isAssigned = true;
    }

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

    public void changeStoreImages(List<String> storeImages)
    {
        List<StoreImage> result = storeImages.stream().map(si -> new StoreImage(si)).collect(Collectors.toList());
        for (StoreImage storeImage : result) {
            this.storeImages.add(storeImage);
        }

    }

    public void setIsBooked(boolean check)
    {
        this.isBooked = check;
    }

    public Store(String name,  String call, Comment comment, List<Opentime> time, String address,String addressMap, boolean isKids,List<Long> categoryIds, List<Long> convenienceIds,Long userSeq) {
        this.name = name; // 가게 이름
        this.call = call; // 가게 전화번호 주소는 잠시 삭제
        this.comment =
                comment;// 값 타입 생성자에서 생성
        this.address = address;
        this.addressMap = addressMap;
      //  this.bookmarkCount = 0;
     //   this.reviewCount = 0;
        this.time = time; // 영업 시간
        this.isKids = isKids; // 키즈존 여부
        this.isAssigned = false;
        this.userSeq = userSeq;
        categoryIds.forEach(c->this.getCategoryIds().add(c));
        convenienceIds.forEach(cv->this.getConvenienceIds().add(cv));
    }

    public Store(Long id, String name,  String call, Comment comment, List<Opentime> time, String address, boolean isKids,List<Long> categoryIds, List<Long> convenienceIds, List<BookMark> bookMarks) {
        this.id = id;
        this.name = name; // 가게 이름
        this.call = call; // 가게 전화번호 주소는 잠시 삭제
        this.comment = comment;// 값 타입 생성자에서 생성
        this.address = address;
        this.bookMarks = bookMarks;
        this.time = time; // 영업 시간
        this.isKids = isKids; // 키즈존 여부
        this.isAssigned = false;

        categoryIds.forEach(c->this.getCategoryIds().add(c));
        convenienceIds.forEach(cv->this.getConvenienceIds().add(cv));
    }

    public void updateStore(String name, boolean kids, String simpleComment, List<Tag> tags, String address, List<String> time, String detailComment, String addressMap, List<String> fileNameList, List<Long> convenienceIds, List<Long> categoryIds, List<Menu> menuList)
    {
       this.name = name;
       this.isKids = kids;
       this.comment  = new Comment(simpleComment,detailComment);
       this.address = address;
       this.addressMap = addressMap;
       // 그냥 변경된 리스트는 새로 끼워넣는걸로 할까? 아예 교체
       this.time = time.stream().map(t -> new Opentime(t)).collect(Collectors.toList());
       // 이미지도 다 날려버리기?
       // 수정 안해도 날리면 비효율인데...
       this.convenienceIds  = convenienceIds;
       this.categoryIds = categoryIds;
       this.setStoreImages(fileNameList);
       setMenus(menuList);
    }
    // TODO : 1. 메뉴를 추가만 하기 2. 삭제만 하기 3. 추가와 삭제 같이 4. 기존 데이터 변경만
    // 경우의 수가 많을때는 그냥 대체 해버리는 걸로?

    public static Store createStore(String name, String call, Comment comment, List<Opentime> time,String address, String addressMap,boolean isKids,  List<Long> categoryIds, List<Long> convenienceIds, List<Menu> menus, List<String> storeImages,Long userSeq)
    {
        Store store = new Store(name,call,comment,time,address,addressMap,isKids,categoryIds, convenienceIds,userSeq);
        store.setMenus(menus);
        store.setStoreImages(storeImages);
        return store;
    }

    public static Store createStoreForUser(String name, String call,String address ,boolean isKids,  List<Long> categoryIds, List<Long> convenienceIds, List<String> storeImages,Long userSeq)
    {
        Store store = new Store(name,call,address,isKids,categoryIds, convenienceIds,userSeq);
      //  store.setMenus(menus);
        store.setStoreImages(storeImages);
        return store;
    }


}
