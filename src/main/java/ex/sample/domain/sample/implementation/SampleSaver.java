package ex.sample.domain.sample.implementation;

import ex.sample.domain.model.Money;
import ex.sample.domain.sample.domain.Sample;
import ex.sample.domain.sample.domain.SampleRepository;
import ex.sample.global.annotation.Implementation;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Implementation
@RequiredArgsConstructor
public class SampleSaver {

    private final SampleRepository sampleRepository;

    public Sample create(Sample sample) {
        return sampleRepository.save(sample);
    }

    public Sample create(String name, BigDecimal money) {
        Sample sample = Sample.create(name, Money.ofKRW(money));
        return sampleRepository.save(sample);
    }
}
