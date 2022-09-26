package com.example.user.controller;


import com.example.user.dto.APIResponse;
import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.exception.UserGroupNotFoundException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import com.example.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<APIUserResponseDTO>> findAllUserWithSorting(@RequestParam (defaultValue = "asc") String orderBy, @RequestParam (defaultValue = "userId") String sort){
        List<APIUserResponseDTO> APIUserResponseDTO = userService.findAllUserWithSorting(orderBy, sort).stream().map(this::convertToDTO).collect(Collectors.toList());
        return new APIResponse<>(APIUserResponseDTO.size(), APIUserResponseDTO);
    }

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createUser(@Valid @RequestBody APIUserRequestDTO APIUserRequestDTO){
        User newUser = modelMapper.map(APIUserRequestDTO,User.class);
        return userService.createUser(newUser);
   }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<APIUserResponseDTO> findUserResponseById(@PathVariable Integer userId) throws UserNotFoundException {
        return ResponseEntity.ok(convertToDTO(userService.findUserById(userId)));
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserById(@Valid @PathVariable Integer userId, @RequestBody APIUserRequestDTO APIUserRequestDTO){
        userService.updateUserById(APIUserRequestDTO,userId);
    }

    @PostMapping("/{userId}/{userGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public void addUserToUserGroup(@PathVariable Integer userId,@PathVariable Integer userGroupId) throws UserNotFoundException, UserGroupNotFoundException {
        userService.addUserToUserGroup(userId,userGroupId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable Integer userId){
        userService.deleteUserById(userId);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public Page<APIUserResponseDTO> getUserWithOffSetPageSize(@PathVariable int offset,
                                                              @PathVariable int pageSize,
                                                              @RequestParam (defaultValue = "asc") String orderBy,
                                                              @RequestParam (defaultValue = "userId") String sort){
        return userService.findUserWithPagination(offset,pageSize, orderBy, sort).map(this::convertToDTO);
    }

//    private APIUserResponseDTO mapToUserResponse(User user){
//        return APIUserResponseDTO.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .mobile(user.getMobile())
//                .build();
//    }

    private APIUserResponseDTO convertToDTO(User user){
        APIUserResponseDTO apiUserResponseDTO = modelMapper.map(user,APIUserResponseDTO.class);
        return apiUserResponseDTO;
    }

}
