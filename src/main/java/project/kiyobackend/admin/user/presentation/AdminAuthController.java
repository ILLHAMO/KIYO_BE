package project.kiyobackend.admin.user.presentation;

import com.nimbusds.openid.connect.sdk.LogoutRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.admin.user.application.AdminAuthService;
import project.kiyobackend.admin.user.application.dto.AdminResponseDto;
import project.kiyobackend.admin.user.presentation.dto.AdminLoginRequest;
import project.kiyobackend.admin.user.presentation.dto.AdminLoginResponse;
import project.kiyobackend.auth.common.LogoutDto;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.auth.token.UserRefreshToken;
import project.kiyobackend.common.SuccessResponseDto;
import project.kiyobackend.common.util.auth.CookieUtil;
import project.kiyobackend.common.util.auth.HeaderUtil;
import project.kiyobackend.user.domain.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


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

    @Operation(summary = "관리자 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> adminLogout(@RequestBody LogoutRequestDto logoutRequestDto,HttpServletRequest request)
    {
        String accessToken = HeaderUtil.getAccessToken(request);
        String result = adminAuthService.logout(accessToken, logoutRequestDto.getRefreshToken());

        return ResponseEntity.ok(result);
    }



}
