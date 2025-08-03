package ex.sample.domain.sample.service;

import ex.sample.domain.sample.domain.Sample;
import ex.sample.domain.sample.dto.response.GetSampleRes;
import ex.sample.domain.sample.implementation.SampleReader;
import ex.sample.domain.sample.mapper.SampleMapper;
import ex.sample.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleQueryService {

    private final SampleReader sampleReader;
    private final SampleMapper sampleMapper;

    /**
     * 샘플 단건 조회
     */
    @Transactional(readOnly = true)
    public GetSampleRes getSample(Long id) {
        Sample sample = sampleReader.read(id);
        return sampleMapper.toGetSampleRes(sample);
    }

    /**
     * 샘플 리스트 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<GetSampleRes> getSampleList(Pageable pageable) {
        Page<GetSampleRes> pages = sampleReader.readAll(pageable)
            .map(sampleMapper::toGetSampleRes);

        return PageResponse.from(pages);
    }
}
