package com.example.user.repository;

import com.example.user.BaseTestContainer;
import com.example.user.model.User;
import com.example.usergroup.model.UserGroup;
import com.example.usergroup.repository.UserGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest  extends BaseTestContainer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Test
    void testCreateUserGroups(){
        UserGroup premium = new UserGroup(1,"Premium",new HashSet<>());
        UserGroup economy = new UserGroup(2,"economy",new HashSet<>());
        UserGroup admin = new UserGroup(3,"admin",new HashSet<>());
        userGroupRepository.saveAll(List.of(premium,economy,admin));
        List<UserGroup> listUserGroups = userGroupRepository.findAll();
        listUserGroups.forEach(System.out::println);
        assertThat(listUserGroups).hasSize(3);
    }

    @Test
    void testAddUserGroupToUser(){
        UserGroup premium = new UserGroup(1,"Premium",new HashSet<>());
        userGroupRepository.saveAll(List.of(premium));
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
        System.out.println(savedUser);
        assertThat(testUser.getUserGroupSet()).hasSize(1);

    }

//    @Test
//    void testAddUserGroupsToExistingUser(){
////        User testUser = User.builder()
////                .userId(1)
////                .name("Cocobopper")
////                .mobile("07809886999")
////                .email("jessie@email.com")
////                .userGroupSet(new HashSet<>())
////                .build();
////        userRepository.save(testUser);
////        UserGroup premium = new UserGroup(1,"Premium",new HashSet<>());
//        User testUser = userRepository.findById(1).get();
////        userGroupRepository.saveAll(List.of(premium));
//        UserGroup userGroup = userGroupRepository.findById(1).get();
//        testUser.addUserGroup(userGroup);
//        userGroup.addUser(testUser);
//        userRepository.save(testUser);
//        userGroupRepository.save(userGroup);
//        assertThat(testUser.getUserGroupSet()).hasSize(1);
//
//    }
//
//    @Test
//    void shouldReturnAllUsers(){
////        User testUser = User.builder()
////                .name("Jessiebopper")
////                .mobile("07809886999")
////                .email("jessie@email.com")
////                .build();
////        userRepository.save(testUser);
//
//        List<User> result = new ArrayList<>(userRepository.findAll());
//        assertEquals(1,result.size(),"that's it only one element");
//    }
//
//    @Test
//    void addUserToGroup(){
//        User testUser = User.builder()
//                .name("Jessiebopper")
//                .mobile("07809886999")
//                .email("jessie@email.com")
//                .build();
//        userRepository.save(testUser);
//    }
}