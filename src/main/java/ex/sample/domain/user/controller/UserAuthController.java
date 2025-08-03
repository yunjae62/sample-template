package ex.sample.domain.user.controller;

import ex.sample.domain.user.dto.request.UserSignupRequest;
import ex.sample.domain.user.dto.request.UserWithdrawalRequest;
import ex.sample.domain.user.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    public void signup(
        @Valid @RequestBody UserSignupRequest request
    ) {
        userAuthService.signup(request);
    }

    @DeleteMapping
    public void delete(
        @AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody UserWithdrawalRequest request
    ) {
        userAuthService.deleteUser(userDetails, request);
    }
}
