package com.example.usergroup.model;

import com.example.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Setter
@Getter
@Table(name = "USR_GRP")
@Entity
public class UserGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int userGroupId;

    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "userGroupSet",fetch = FetchType.LAZY)
    private Set<User> userSet = new HashSet<>();

    public void addUser(User user){
        this.userSet.add(user);
    }

}
