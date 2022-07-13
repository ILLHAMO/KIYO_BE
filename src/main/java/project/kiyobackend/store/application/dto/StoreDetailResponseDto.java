package project.kiyobackend.store.application.dto;

import lombok.Data;
import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.store.StoreImage;

import java.util.List;

// TODO : 상세 페이지 쿼리 최적화 공부!
@Data
public class StoreDetailResponseDto {

    private Long id;
    private boolean kids;
    private String name;
    private String comment;
    private String call;
    private String address;
    private String addressMap;
    private String time;
    private String detailComment;
    private List<String> storeImages;
    private List<Long> service;
    private List<Menu> menus;
    private List<Review> reviews;


}
