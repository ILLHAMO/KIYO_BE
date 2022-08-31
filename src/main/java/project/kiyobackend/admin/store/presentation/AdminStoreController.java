package project.kiyobackend.admin.store.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.admin.store.application.StoreAdminService;
import project.kiyobackend.admin.store.presentation.dto.StoreDetailResponseForUpdate;
import project.kiyobackend.admin.store.presentation.dto.StoreQueryDto;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.store.presentation.dto.store.StoreDetailResponse;
import project.kiyobackend.store.presentation.dto.store.StoreDetailResponseDto;
import project.kiyobackend.store.query.dto.StorePaginationDto;
import project.kiyobackend.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminStoreController {

    private final StoreAdminService storeAdminService;

    @Operation(summary = "신규 가게 승인")
    @PutMapping("/store/{storeId}/assign")
    public ResponseEntity<Boolean> assignStore(@CurrentUser User adminUser, @PathVariable Long storeId)
    {
        return ResponseEntity.ok(storeAdminService.assignStore(storeId));
    }

    @Operation(summary = "가게 정보 수정을 위한 데이터 조회")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<StoreDetailResponseForUpdate> getStoreForUpdate(@PathVariable Long storeId)
    {
        return ResponseEntity.ok(storeAdminService.getStoreDetailForUpdate(storeId));
    }

    @Operation(summary = "가게 정보 업데이트")
    @PutMapping("/store/{storeId}")
    public void updateStoreInfo(@PathVariable Long storeId)
    {

    }


    // TODO : 카운트 쿼리 최적화 적용 & 블로그에 작성하기
    @Operation(summary = "관리자를 위한 가게 목록 페이징")
    @GetMapping("/store")
    public Page<StorePaginationDto> getStore(StoreQueryDto storeQueryDto, Pageable pageable)
    {
           return storeAdminService.getStore(storeQueryDto,pageable);
    }


}
