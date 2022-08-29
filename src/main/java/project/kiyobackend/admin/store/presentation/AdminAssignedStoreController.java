package project.kiyobackend.admin.store.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.admin.store.presentation.dto.StoreQueryDto;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminAssignedStoreController {

    private final StoreService storeService;

    @Operation(summary = "신규 가게 승인")
    @PutMapping("/store/{storeId}/assign")
    public ResponseEntity<Boolean> assignStore(@CurrentUser User adminUser, @RequestParam Long storeId)
    {
        return ResponseEntity.ok(storeService.assignStore(storeId));
    }

    @Operation(summary = "가게 정보 수정을 위한 데이터 조회")
    @GetMapping("/store/{storeId}")
    public void getStoreForUpdate(@CurrentUser User adminUser,@RequestParam Long storeId)
    {

    }

    @Operation(summary = "관리자를 위한 가게 목록 페이징")
    @GetMapping("/store")
    public void getStore(@CurrentUser User adminUser, StoreQueryDto storeQueryDto, Pageable pageable)
    {

    }


}
