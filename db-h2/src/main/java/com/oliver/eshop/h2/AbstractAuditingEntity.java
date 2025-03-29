package com.oliver.eshop.h2;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AbstractAuditingEntity {

    @Version
    private Integer version;
    @CreatedDate
    public OffsetDateTime createdAt;
    @LastModifiedDate
    public OffsetDateTime modifiedAt;
}
