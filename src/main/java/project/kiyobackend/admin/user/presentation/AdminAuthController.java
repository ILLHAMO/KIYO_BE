package project.kiyobackend.admin.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.admin.user.application.AdminAuthService;
import project.kiyobackend.admin.user.application.dto.AdminResponseDto;
import project.kiyobackend.admin.user.presentation.dto.AdminLoginRequest;
import project.kiyobackend.admin.user.presentation.dto.AdminLoginResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(summary = "관리자 로그인")
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> adminLogin(@RequestBody AdminLoginRequest adminLoginRequest)
    {
        AdminResponseDto signin = adminAuthService.signin(adminLoginRequest.getAdminId(), adminLoginRequest.getPassword());
        return ResponseEntity.ok(new AdminLoginResponse(signin.getAccessToken(),signin.getRefreshToken()));
    }

}
