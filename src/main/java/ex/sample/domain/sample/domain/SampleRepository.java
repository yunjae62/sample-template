package ex.sample.domain.sample.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleRepository {

    Optional<Sample> findById(Long id);

    Page<Sample> findAll(Pageable pageable);

    Sample save(Sample sample);
}
