package com.example.usergroup.service;

import com.example.usergroup.dto.UserGroupRequest;
import com.example.usergroup.dto.UserGroupDTOResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserGroupService {

    List<UserGroupDTOResponse> findAllUserGroups();

    public ResponseEntity<String> createUserGroup(UserGroupRequest userGroupRequest);

    public Optional<UserGroupDTOResponse> findUserGroupById(Integer id);

}
