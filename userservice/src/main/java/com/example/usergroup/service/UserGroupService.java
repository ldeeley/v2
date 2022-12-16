package com.example.usergroup.service;

import com.example.user.dto.APIUserResponseDTO;
import com.example.usergroup.dto.APIUserGroupRequestDTO;
import com.example.usergroup.dto.APIUserGroupResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserGroupService {

    List<APIUserGroupResponseDTO> findAllUserGroups();

    void createUserGroup(APIUserGroupRequestDTO apiUserGroupRequestDTO);

    Optional<APIUserGroupResponseDTO> findUserGroupById(Integer id);

    List<APIUserResponseDTO> findByuserGroupIdLike(Integer userGroupId);

}
