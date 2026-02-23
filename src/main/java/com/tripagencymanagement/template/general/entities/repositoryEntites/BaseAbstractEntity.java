package com.tripagencymanagement.template.general.entities.repositoryEntites;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseAbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @lombok.Builder.Default // Para que aparezca en el builder con valor por defecto
    @Setter
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_date", updatable = false) // no se debe poder modificar una vez creado
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedDate;
}
