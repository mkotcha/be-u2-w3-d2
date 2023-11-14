package org.emmek.beu2w3d2.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @OneToMany(mappedBy = "user")
    @ToStringExclude
    @JsonIgnore
    @ToString.Exclude
    List<Device> devices;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String avatar;
}
