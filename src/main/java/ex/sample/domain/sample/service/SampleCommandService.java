package ex.sample.domain.sample.service;

import ex.sample.domain.sample.domain.Sample;
import ex.sample.domain.sample.dto.request.CreateSampleReq;
import ex.sample.domain.sample.dto.response.CreateSampleRes;
import ex.sample.domain.sample.implementation.SampleSaver;
import ex.sample.domain.sample.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleCommandService {

    private final SampleSaver sampleSaver;
    private final SampleMapper sampleMapper;

    /**
     * 샘플 생성
     */
    @Transactional
    public CreateSampleRes createSample(CreateSampleReq request) {
        Sample sample = sampleSaver.create(request.name(), request.money());
        return sampleMapper.toCreateSampleRes(sample);
    }
}
