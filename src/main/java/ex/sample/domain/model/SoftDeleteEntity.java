package ex.sample.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SoftDeleteEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
