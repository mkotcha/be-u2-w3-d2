package org.emmek.beu2w3d2.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private long id;
    private String category;
    private String state;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
