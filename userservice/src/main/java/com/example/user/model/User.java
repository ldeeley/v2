package com.example.user.model;

import com.example.usergroup.model.UserGroup;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "USER")
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int userId;
    private String name;
    private String email;
    private String mobile;
    private String topSecretData = "topSecretData";

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "USER_USR_GRP",
            joinColumns = {
                @JoinColumn(name = "USR_ID",referencedColumnName = "userId")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "USR_GRP_ID",referencedColumnName = "userGroupId")
            }
    )
    private Set<UserGroup> userGroupSet = new HashSet<>();

    public void addUserGroup(UserGroup userGroup){
        this.userGroupSet.add(userGroup);
    }

}
