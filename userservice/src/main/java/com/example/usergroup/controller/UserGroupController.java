package com.example.usergroup.controller;

import com.example.user.dto.APIResponse;
import com.example.usergroup.dto.UserGroupRequest;
import com.example.usergroup.dto.UserGroupDTOResponse;
import com.example.usergroup.service.UserGroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/usergroups")
@RestController
public class UserGroupController {

    private final UserGroupServiceImpl userGroupService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<UserGroupDTOResponse>> findAllUserGroupWithSorting(@RequestParam (defaultValue = "asc") String orderBy, @RequestParam (defaultValue = "userGroupId") String sort){
        List<UserGroupDTOResponse> userGroupDTORespons = userGroupService.findAllUserGroupsWithSorting(orderBy, sort);
        return new APIResponse<>(userGroupDTORespons.size(), userGroupDTORespons);
    }

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createUserGroup(@RequestBody UserGroupRequest userGroupRequest){
        return userGroupService.createUserGroup(userGroupRequest);
    }

    @GetMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<UserGroupDTOResponse> findUserGroupResponseById(@PathVariable Integer userGroupId){
        return userGroupService.findUserGroupById(userGroupId);
    }

    @PutMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserGroupById(@PathVariable Integer userGroupId, @RequestBody UserGroupRequest userGroupRequest){
        userGroupService.updateUserGroupById(userGroupRequest,userGroupId);

    }

    @DeleteMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserGroupById(@PathVariable Integer userGroupId){
        userGroupService.deleteUserGroupById(userGroupId);
    }


    @GetMapping("/pagination/{offset}/{pageSize}")
    public Page<UserGroupDTOResponse> getUserGroupWithOffSetPageSize(@PathVariable int offset,
                                                                     @PathVariable int pageSize,
                                                                     @RequestParam (defaultValue = "asc") String orderBy,
                                                                     @RequestParam (defaultValue = "userGroupId") String sort){
        return userGroupService.findUserGroupWithPagination(offset,pageSize, orderBy, sort);
    }

}
