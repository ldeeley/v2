package com.example.user.service;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.exception.UserGroupNotFoundException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<User> findAllUserWithSorting(String orderBy, String field);
    public ResponseEntity<String> createUser(User user);
    public User findUserById(Integer id) throws UserNotFoundException;
    public void updateUserById(APIUserRequestDTO APIUserRequestDTO, Integer userId);
    public void deleteUserById(Integer id);
    Page<User> findUserWithPagination(int offset, int pageSize, String orderBy, String field);

    List<User> findAllUsers();
    Page<User> findUserWithPaginationAndSort(int offset, int pageSize, String field);
    public Page<User> findPaginated(int offset, int pageSize);

    Iterable<User> saveUserList(List<User> userList);

    public void addUserToUserGroup(Integer userId, Integer userGroupId) throws UserNotFoundException, UserGroupNotFoundException;
}
