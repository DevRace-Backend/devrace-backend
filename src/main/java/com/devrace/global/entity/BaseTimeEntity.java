package com.devrace.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
    }

}
