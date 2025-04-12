package ex.sample.domain.sample.implementation;

import ex.sample.domain.sample.domain.Sample;
import ex.sample.domain.sample.domain.SampleRepository;
import ex.sample.global.annotation.Implementation;
import ex.sample.global.exception.GlobalException;
import ex.sample.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Implementation
@RequiredArgsConstructor
public class SampleReader {

    private final SampleRepository sampleRepository;

    public Sample read(Long id) {
        return sampleRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ResponseCode.NOT_FOUND));
    }

    public Page<Sample> readAll(Pageable pageable) {
        return sampleRepository.findAll(pageable);
    }
}
