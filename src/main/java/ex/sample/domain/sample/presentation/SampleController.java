package ex.sample.domain.sample.presentation;

import ex.sample.domain.sample.application.SampleCommandService;
import ex.sample.domain.sample.application.SampleQueryService;
import ex.sample.domain.sample.dto.request.CreateSampleReq;
import ex.sample.domain.sample.dto.response.CreateSampleRes;
import ex.sample.domain.sample.dto.response.GetSampleRes;
import ex.sample.global.response.CommonRes;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public CommonRes<GetSampleRes> getSample(@PathVariable("id") UUID id) {
        GetSampleRes response = sampleQueryService.getSample(id);
        return CommonRes.success(response);
    }

    /**
     * 샘플 리스트 조회
     */
    @GetMapping
    public CommonRes<Slice<GetSampleRes>> getSample(
        @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        Slice<GetSampleRes> response = sampleQueryService.getSampleList(pageable);
        return CommonRes.success(response);
    }

    /**
     * 샘플 생성
     */
    @PostMapping
    public CommonRes<CreateSampleRes> createSample(@Validated @RequestBody CreateSampleReq request) {
        CreateSampleRes response = sampleCommandService.createSample(request);
        return CommonRes.success(response);
    }
}
