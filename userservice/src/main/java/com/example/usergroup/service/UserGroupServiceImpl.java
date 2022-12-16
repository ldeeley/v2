package com.example.usergroup.service;

import com.example.user.dto.APIUserResponseDTO;
import com.example.user.repository.UserRepository;
import com.example.usergroup.dto.APIUserGroupRequestDTO;
import com.example.usergroup.dto.APIUserGroupResponseDTO;
import com.example.usergroup.model.UserGroup;
import com.example.usergroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserGroupServiceImpl implements UserGroupService{

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

//    @Cacheable("userGroups")
    public List<APIUserGroupResponseDTO> findAllUserGroups(){
        List<UserGroup> userGroupList = new ArrayList<>();
        userGroupRepository.findAll().forEach(userGroupList::add);
        return userGroupList.stream().map(this::mapToUserGroupResponse).toList();
    }

    public List<APIUserGroupResponseDTO> findAllUserGroupsWithSorting(String orderBy, String field){
        List<UserGroup> userGroupList = new ArrayList<>();
        userGroupList.addAll(userGroupRepository.findAll(Sort.by( getSortDirection(orderBy), field)));
        return userGroupList.stream().map(this::mapToUserGroupResponse).toList();
    }

    public void updateUserGroupById(APIUserGroupRequestDTO apiUserGroupRequestDTO, Integer userGroupId) {

            UserGroup userGroup = UserGroup.builder()
                    .userGroupId(userGroupId)
                    .title(apiUserGroupRequestDTO.getTitle())
                    .build();
            userGroupRepository.save(userGroup);
            log.info("UserGroup {} is updated", userGroup.getTitle());

    }

    public void deleteUserGroupById(Integer userGroupId){
        userGroupRepository.deleteById(userGroupId);
    }


    @Transactional(rollbackOn = SQLException.class)
    @Override
    public void createUserGroup(APIUserGroupRequestDTO apiUserGroupRequestDTO) {

            UserGroup userGroup = UserGroup.builder()
                    .title(apiUserGroupRequestDTO.getTitle())
                    .build();
            userGroupRepository.save(userGroup);
            log.info("UserGroup {} is saved", userGroup.getTitle());

    }


//    @Cacheable(value="userGroup",key="#p0")
    public Optional<APIUserGroupResponseDTO> findUserGroupById(Integer userGroupId){
        Optional<UserGroup> userGroup = userGroupRepository.findById(userGroupId);
        return userGroup.map(this::mapToUserGroupResponse);
    }

    @Override
    public List<APIUserResponseDTO> findByuserGroupIdLike(Integer userGroupId) {
//        List<APIUserResponseDTO> apiUserResponseDTOList = userRepository.findB
        return null;
    }

    private APIUserGroupResponseDTO mapToUserGroupResponse(UserGroup userGroup){
        return APIUserGroupResponseDTO.builder()
                .userGroupId(userGroup.getUserGroupId())
                .title(userGroup.getTitle())
                .build();
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public Page<APIUserGroupResponseDTO> findUserGroupWithPagination(int offset, int pageSize, String orderBy, String field){

        return userGroupRepository.findAll(PageRequest.of(offset-1, pageSize).withSort(getSortDirection(orderBy),field)).map(this::mapToUserGroupResponse);
    }

}
