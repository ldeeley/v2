package com.example.usergroup.controller;

import com.example.user.dto.APIResponse;
import com.example.usergroup.dto.APIUserGroupRequestDTO;
import com.example.usergroup.dto.APIUserGroupResponseDTO;
import com.example.usergroup.service.UserGroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public APIResponse<List<APIUserGroupResponseDTO>> findAllUserGroupWithSorting(@RequestParam (defaultValue = "asc") String orderBy, @RequestParam (defaultValue = "userGroupId") String sort){
        List<APIUserGroupResponseDTO> userGroupDTOResponse = userGroupService.findAllUserGroupsWithSorting(orderBy, sort);
        return new APIResponse<>(userGroupDTOResponse.size(), userGroupDTOResponse);
    }

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserGroup(@RequestBody APIUserGroupRequestDTO apiUserGroupRequestDTO){
        userGroupService.createUserGroup(apiUserGroupRequestDTO);
    }

    @GetMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<APIUserGroupResponseDTO> findUserGroupResponseById(@PathVariable Integer userGroupId){
        return userGroupService.findUserGroupById(userGroupId);
    }

    @PutMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserGroupById(@PathVariable Integer userGroupId, @RequestBody APIUserGroupRequestDTO apiUserGroupRequestDTO){
        userGroupService.updateUserGroupById(apiUserGroupRequestDTO,userGroupId);
    }

    @DeleteMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserGroupById(@PathVariable Integer userGroupId){
        userGroupService.deleteUserGroupById(userGroupId);
    }


    @GetMapping("/pagination/{offset}/{pageSize}")
    public Page<APIUserGroupResponseDTO> getUserGroupWithOffSetPageSize(@PathVariable int offset,
                                                                        @PathVariable int pageSize,
                                                                        @RequestParam (defaultValue = "asc") String orderBy,
                                                                        @RequestParam (defaultValue = "userGroupId") String sort){
        return userGroupService.findUserGroupWithPagination(offset,pageSize, orderBy, sort);
    }

}
