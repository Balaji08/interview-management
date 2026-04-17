package com.project.interviewmanagement_service.interviewer.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Entity
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class Interviewer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private  String name;

    private String expertise;






}
