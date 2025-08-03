package ex.sample.domain.sample.controller;

import ex.sample.domain.sample.dto.request.CreateSampleRequest;
import ex.sample.domain.sample.dto.response.CreateSampleResponse;
import ex.sample.domain.sample.dto.response.GetSampleResponse;
import ex.sample.domain.sample.service.SampleCommandService;
import ex.sample.domain.sample.service.SampleQueryService;
import ex.sample.global.response.ApiResponse;
import ex.sample.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/samples") // URL 자원은 복수형으로 사용
public class SampleController {

    private final SampleQueryService sampleQueryService;
    private final SampleCommandService sampleCommandService;

    /**
     * 샘플 단건 조회
     */
    @GetMapping("/{id}")
    public ApiResponse<GetSampleResponse> getSample(@PathVariable("id") Long id) {
        GetSampleResponse response = sampleQueryService.getSample(id);
        return ApiResponse.success(response);
    }

    /**
     * 샘플 리스트 조회
     */
    @GetMapping
    public ApiResponse<PageResponse<GetSampleResponse>> getSample(
        @PageableDefault(size = 20, sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        PageResponse<GetSampleResponse> response = sampleQueryService.getSampleList(pageable);
        return ApiResponse.success(response);
    }

    /**
     * 샘플 생성
     */
    @PostMapping
    public ApiResponse<CreateSampleResponse> createSample(@Validated @RequestBody CreateSampleRequest request) {
        CreateSampleResponse response = sampleCommandService.createSample(request);
        return ApiResponse.success(response);
    }
}
