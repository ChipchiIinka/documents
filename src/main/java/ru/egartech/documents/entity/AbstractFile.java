package ru.egartech.documents.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractFile {
    @Column(name = "name", unique = true,  nullable = false, length = 100)
    private String name;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "size", nullable = false)
    private Long size;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", nullable = false)
    private byte[] data;
}
