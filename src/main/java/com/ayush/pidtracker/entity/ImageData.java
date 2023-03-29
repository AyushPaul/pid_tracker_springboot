package com.ayush.pidtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "ImageData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    @Lob
    @Column(name = "imagedata",length = 65555)
    private byte[] imageData;

    private String comment;

    private String user_id;

    private String reviewer_id;

    @Column(columnDefinition = "boolean default false")
    private Boolean reviewed;

    @Temporal(TemporalType.DATE)
    Date creationDate;
}