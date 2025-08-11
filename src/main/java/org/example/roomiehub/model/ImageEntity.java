package org.example.roomiehub.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String contentType;

    @Lob
    @Column(columnDefinition = "LONGBLOB") // MySQL lưu ảnh dạng LONGBLOB
    private byte[] data;
}
