package project.kiyobackend.store.adapter.presentation;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


class StoreControllerTest {

    @Test
    @DisplayName("lastStoreId와 size가 주어졌을때 no-offset 방식으로 페이징 잘 되는지 테스트")
    void main_page()
    {
        get("http://localhost:8080/api/stores?size=6&lastStoreId=7").then().body("content[0].id", equalTo(6));
        get("http://localhost:8080/api/stores?size=6&lastStoreId=7").then().body("content[5].id", equalTo(1));
        get("http://localhost:8080/api/stores?size=6&lastStoreId=7").then().body("last", equalTo(true));
    }

}