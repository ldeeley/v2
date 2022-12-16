package com.example.user.service;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.exception.UserGroupNotFoundException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<APIUserResponseDTO> findAllUserWithSorting(String orderBy, String field);
    public void createUser(APIUserRequestDTO apiUserRequestDTO);
    public APIUserResponseDTO findUserById(Integer id) throws UserNotFoundException;
    public void updateUserById(APIUserRequestDTO apiUserRequestDTO, Integer userId);
    public ResponseEntity<String> deleteUserById(Integer id);
    Page<APIUserResponseDTO> findUserWithPagination(int offset, int pageSize, String orderBy, String field);

    List<User> findAllUsers();
    Page<APIUserResponseDTO> findUserWithPaginationAndSort(int offset, int pageSize, String field);
    public Page<APIUserResponseDTO> findPaginated(int offset, int pageSize);

    Iterable<User> saveUserList(List<User> userList);

    public void addUserToUserGroup(Integer userId, Integer userGroupId) throws UserNotFoundException, UserGroupNotFoundException;
}
