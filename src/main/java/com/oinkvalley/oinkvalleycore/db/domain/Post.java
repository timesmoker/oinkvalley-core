package com.oinkvalley.oinkvalleycore.db.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts", schema = "oinkvalley_local_dev_schema")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('oinkvalley_local_dev_schema.posts_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 100)
    @NotNull
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @NotNull
    @Column(name = "content", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> content;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull
    @Column(name = "board_type", nullable = false, length = 30)
    private String boardType;

    @Column(name = "author_name", nullable = false, length = 30)
    private String authorName;

    @ColumnDefault("now()")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<Image> images = new LinkedHashSet<>();

}