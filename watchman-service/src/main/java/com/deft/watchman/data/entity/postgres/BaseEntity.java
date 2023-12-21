package com.deft.watchman.data.entity.postgres;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author Sergey Golitsyn
 * created on 05.10.2023
 *
 * Base entity for all other entities
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -278971545601962170L;

    @Id
    @UuidGenerator
    private String id;

    private Instant createDate;
    private Instant updateDate;

    @PrePersist
    void onCreate() {
        this.createDate = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updateDate = Instant.now();
    }
}
