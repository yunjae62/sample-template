package ex.sample.domain.user.controller;

import ex.sample.domain.user.dto.request.UserSignupReq;
import ex.sample.domain.user.dto.request.UserWithdrawalReq;
import ex.sample.domain.user.service.UserAuthService;
import ex.sample.global.response.ApiResponse;
import ex.sample.global.response.EmptyResponse;
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
    public ApiResponse<EmptyResponse> signup(
        @Valid @RequestBody UserSignupReq request
    ) {
        EmptyResponse response = userAuthService.signup(request);
        return ApiResponse.success(response);
    }

    @DeleteMapping
    public ApiResponse<EmptyResponse> delete(
        @AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody UserWithdrawalReq request
    ) {
        EmptyResponse response = userAuthService.deleteUser(userDetails, request);
        return ApiResponse.success(response);
    }
}
