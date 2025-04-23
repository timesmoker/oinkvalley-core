package com.oinkvalley.oinkvalleycore.db.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "images", schema = "oinkvalley_local_dev_schema")
public class Image {
    @Id
    @ColumnDefault("nextval('oinkvalley_local_dev_schema.images_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Column(name = "filename", nullable = false, length = Integer.MAX_VALUE)
    private String filename;

    @NotNull
    @Column(name = "url", nullable = false, length = Integer.MAX_VALUE)
    private String url;

    @ColumnDefault("now()")
    @Column(name = "uploaded_at")
    private Instant uploadedAt;

}