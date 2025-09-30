package io.github.dan7arievlis.marketplaceapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "flg_active")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    private User user;

    public UUID getLastUpdatedByUser() {
        return user.getId();
    }

    public Base getMetaData() {
        return this;
    }

    public void delete() {
        setDeletedAt(LocalDateTime.now());
        setActive(false);
    }

    public void restore() {
        setDeletedAt(null);
        setActive(true);
    }
}
