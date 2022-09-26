package com.example.usergroup.service;

import com.example.usergroup.repository.UserGroupRepository;
import com.example.usergroup.dto.UserGroupRequest;
import com.example.usergroup.dto.UserGroupDTOResponse;
import com.example.usergroup.model.UserGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
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

    @Cacheable("userGroups")
    public List<UserGroupDTOResponse> findAllUserGroups(){
        List<UserGroup> userGroupList = new ArrayList<>();
        userGroupRepository.findAll().forEach(userGroupList::add);
        return userGroupList.stream().map(this::mapToUserGroupResponse).toList();
    }


    public List<UserGroupDTOResponse> findAllUserGroupsWithSorting(String orderBy, String field){
        List<UserGroup> userGroupList = new ArrayList<>();
        userGroupList.addAll(userGroupRepository.findAll(Sort.by( getSortDirection(orderBy), field)));
        return userGroupList.stream().map(this::mapToUserGroupResponse).toList();
    }

    public void updateUserGroupById(UserGroupRequest userGroupRequest, Integer userGroupId) {

            UserGroup userGroup = UserGroup.builder()
                    .userGroupId(userGroupId)
                    .title(userGroupRequest.getTitle())
                    .build();
            saveUserGroup(userGroup);
//            userRepository.save(user);
//            log.info("User {} is saved", user.getEmail());

    }

    public void deleteUserGroupById(Integer userGroupId){
        userGroupRepository.deleteById(userGroupId);
    }

    @Override
    public ResponseEntity<String> createUserGroup(UserGroupRequest userGroupRequest) {

            UserGroup userGroup = UserGroup.builder()
                    .title(userGroupRequest.getTitle())
                    .build();
            saveUserGroup(userGroup);           // is this correct , check with Tom ?
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(URI.create("/rnr/userGroup/"+userGroup.getUserGroupId()));
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }


    @Cacheable(value="userGroup",key="#p0")
    public Optional<UserGroupDTOResponse> findUserGroupById(Integer userGroupId){
        Optional<UserGroup> userGroup = userGroupRepository.findById(userGroupId);
        return userGroup.map(this::mapToUserGroupResponse);
    }

    private UserGroupDTOResponse mapToUserGroupResponse(UserGroup userGroup){
        return UserGroupDTOResponse.builder()
                .userGroupId(userGroup.getUserGroupId())
                .title(userGroup.getTitle())
                .build();
    }

    // added this method but is it really necessary ?
    @Transactional(rollbackOn = SQLException.class)
    void saveUserGroup(UserGroup userGroup){
        userGroupRepository.save(userGroup);
        log.info("User {} is saved", userGroup.getTitle());
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public Page<UserGroupDTOResponse> findUserGroupWithPagination(int offset, int pageSize, String orderBy, String field){

        return userGroupRepository.findAll(PageRequest.of(offset-1, pageSize).withSort(getSortDirection(orderBy),field)).map(this::mapToUserGroupResponse);
    }

}
