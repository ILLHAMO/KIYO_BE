package project.kiyobackend.user.adapter.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kiyobackend.auth.common.UserDto;
import project.kiyobackend.user.application.UserService;
import project.kiyobackend.user.domain.User;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @GetMapping
//    public ResponseEntity<UserDto> getUser() {
//        // 이 방식으로 개인 정보 빼서 사용하면 된다!
//        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        User user = userService.getUser(principal.getUsername());
//
//        return ResponseEntity.ok(new UserDto(200,true,"user",user));
//    }

    @GetMapping("/hello")
    public String hello()
    {
        return "hello";
    }


}
