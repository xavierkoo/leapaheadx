package com.oop.leap_ahead_x.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class FormWorkflow {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    private UUID formUuid;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "\"description\"")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Admin createdBy;

    @OneToMany(mappedBy = "parentForm")
    private Set<FormStep> parentFormFormSteps;

    @OneToMany(mappedBy = "formUuid")
    private Set<Application> formUuidApplications;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @Column
    private boolean archive;


    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }
    public boolean getArchive() {
        return archive;
    }

    public UUID getFormUuid() {
        return formUuid;
    }

    public void setFormUuid(final UUID formUuid) {
        this.formUuid = formUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Admin getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final Admin createdBy) {
        this.createdBy = createdBy;
    }

    public Set<FormStep> getParentFormFormSteps() {
        return parentFormFormSteps;
    }

    public void setParentFormFormSteps(final Set<FormStep> parentFormFormSteps) {
        this.parentFormFormSteps = parentFormFormSteps;
    }

    public Set<Application> getFormUuidApplications() {
        return formUuidApplications;
    }

    public void setFormUuidApplications(final Set<Application> formUuidApplications) {
        this.formUuidApplications = formUuidApplications;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


}
