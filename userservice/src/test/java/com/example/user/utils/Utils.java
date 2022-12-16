package com.example.user.utils;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.model.User;
import com.example.usergroup.dto.APIUserGroupRequestDTO;
import com.example.usergroup.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static User CreateUser(Integer id){
        return User.builder().
                userId(id).
                email("test99@test.com").
                name("tester99").
                mobile("9999999999").
                build();

    }

    public static User CreateUser(Integer id, String userName){
        return User.builder().
                userId(id).
                email("test99@test.com").
                name(userName).
                mobile("9999999999").
                build();

    }


    public static List<User> createUserList(){
        List<User> userList = new ArrayList<>();
        return List.of(CreateUser(1,"Coco"),CreateUser(2,"Gillianca"),CreateUser(3,"littleElge"));

    }

    public static APIUserResponseDTO CreateAPIUserResponseDTO (Integer id){
        return APIUserResponseDTO.builder()
                .userId(99)
                .email("test99@test.com")
                .name("tester99")
                .mobile("9999999999")
                .build();

    }

    public static APIUserRequestDTO CreateAPIUserRequestDTO (String name){
        return APIUserRequestDTO.builder()
                .email("test99@test.com")
                .name(name)
                .mobile("9999999999")
                .build();

    }

    public static APIUserGroupRequestDTO CreateAPIUserGroupRequestDTO (String title){
        return APIUserGroupRequestDTO.builder()
                .title(title)
                .build();
    }


    public static UserGroup CreateUserGroup(Integer id){
        return UserGroup.builder().
                userGroupId(id).
                title("premium").
                build();

    }

    public static UserGroup CreateUserGroup(Integer id, String title){
        return UserGroup.builder().
                userGroupId(id).
                title(title).
                build();

    }

    public static List<UserGroup> createUserGroupList(){
        List<UserGroup> userGroupList = new ArrayList<>();
        return List.of(CreateUserGroup(1,"premium"),CreateUserGroup(2,"economy"),CreateUserGroup(3,"discount"));

    }

}
