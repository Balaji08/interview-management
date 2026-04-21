package com.project.interviewmanagement_service.candidate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
@Entity
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  Long id;

    @NotBlank
    private String name;

    @Column(unique = true)
    private String email;
}
