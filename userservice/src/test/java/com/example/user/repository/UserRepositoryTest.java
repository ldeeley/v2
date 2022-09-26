package com.example.user.repository;

import com.example.user.model.User;
import com.example.usergroup.model.UserGroup;
import com.example.usergroup.repository.UserGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class UserRepositoryTest  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Test
    public void testCreateUserGroups(){
        UserGroup premium = new UserGroup(1,"Premium",new HashSet<>());
        UserGroup economy = new UserGroup(2,"economy",new HashSet<>());
        UserGroup admin = new UserGroup(3,"admin",new HashSet<>());
        userGroupRepository.saveAll(List.of(premium,economy,admin));
        List<UserGroup> listUserGroups = userGroupRepository.findAll();
        assertThat(listUserGroups.size()).isEqualTo(3);
    }

    @Test
    public void testAddUserGroupToUser(){
        User testUser = User.builder()
                .name("Cocobopper")
                .mobile("07809886999")
                .email("jessie@email.com")
                .userGroupSet(new HashSet<>())
                .build();
        userRepository.save(testUser);

        UserGroup userGroup = userGroupRepository.findById(1).get();
        testUser.addUserGroup(userGroup);
        User savedUser = userRepository.save(testUser);
        assertThat(testUser.getUserGroupSet().size()).isEqualTo(1);

    }

    @Test
    public void testAddUserGroupsToExistingUser(){
        User testUser = userRepository.findById(502).get();

        UserGroup userGroup = userGroupRepository.findById(2).get();
        testUser.addUserGroup(new UserGroup(3,"Newcastle",new HashSet<>()));
        userRepository.save(testUser);
        assertThat(testUser.getUserGroupSet().size()).isEqualTo(2);

    }

    @Test
    void shouldReturnAllUsers(){
        User testUser = User.builder()
                .name("Jessiebopper")
                .mobile("07809886999")
                .email("jessie@email.com")
                .build();
        userRepository.save(testUser);

        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        assertEquals(1,result.size(),"that's it only one element");
    }

    @Test
    void addUserToGroup(){
        User testUser = User.builder()
                .name("Jessiebopper")
                .mobile("07809886999")
                .email("jessie@email.com")
                .build();
        userRepository.save(testUser);




    }
}