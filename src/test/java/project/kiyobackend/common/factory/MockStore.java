package project.kiyobackend.common.factory;

import project.kiyobackend.review.domain.domain.Review;
import project.kiyobackend.store.domain.domain.bookmark.BookMark;
import project.kiyobackend.store.domain.domain.menu.Menu;
import project.kiyobackend.store.domain.domain.store.Comment;
import project.kiyobackend.store.domain.domain.store.Opentime;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.store.domain.domain.store.StoreImage;

import java.util.ArrayList;
import java.util.List;

public class MockStore {

    private MockStore()
    {

    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private List<Long> categoryIds = new ArrayList<>();
        private List<Long> convenienceIds = new ArrayList<>();
        private String name = "puradak";
        private String call = "010-2222-1111";
        private Comment comment = new Comment("simple","detail");
        private List<Review> reviews = new ArrayList<Review>();
        private List<StoreImage> storeImages = new ArrayList<StoreImage>();
        private List<BookMark> bookMarks = new ArrayList<BookMark>();
        private List<Menu> menus = new ArrayList<Menu>();
        private int bookmarkCount = 0;
        private int reviewCount = 0;
        private List<Opentime> time = new ArrayList<>();
        private boolean isKids = true;
        private String address = "성동구 왕십리";
        private boolean isBooked = false;
        private boolean isAssigned = true;

        public Builder id(Long id)
        {
            this.id = id;
            return this;
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }


        public Builder categoryIds(List<Long> categoryIds)
        {
            this.categoryIds = categoryIds;
            return this;
        }

        public Builder convenienceIds(List<Long> convenienceIds)
        {
            this.convenienceIds = convenienceIds;
            return this;
        }

        public Builder reviews(List<Review> review)
        {
            this.reviews = review;
            return this;
        }

        public Builder bookmarks(List<BookMark> bookMarks)
        {
            this.bookMarks = bookMarks;
            return this;
        }

        public Store build()
        {
            Store store = new Store(id, name, call, comment, time, address, isKids, categoryIds, convenienceIds,bookMarks);
            store.assignStore();
            return store;
        }
    }


}
