package ex.sample.domain.sample.repository;

import ex.sample.domain.sample.domain.Sample;
import ex.sample.domain.sample.domain.SampleRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface SampleJpaRepository extends SampleRepository, JpaRepository<Sample, Long> {

}
