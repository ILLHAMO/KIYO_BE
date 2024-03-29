package project.kiyobackend.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.store.application.StoreService;
import project.kiyobackend.store.domain.domain.store.Store;
import project.kiyobackend.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminAssignedStoreController {

    private StoreService storeService;

    @Operation(summary = "신규 등록된 가게 승인")
    @PutMapping("/store/{storeId}/assign")
    public ResponseEntity<Boolean> assignStore(@CurrentUser User adminUser, @RequestParam Long storeId)
    {
        return ResponseEntity.ok(storeService.assignStore(storeId));

    }
}
