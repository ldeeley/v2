package com.example.user.service;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<APIUserResponseDTO> findAllUserWithSorting(String orderBy, String field);
    public ResponseEntity<String> createUser(APIUserRequestDTO APIUserRequestDTO);
    public APIUserResponseDTO findUserById(Integer id) throws UserNotFoundException;
    public void updateUserById(APIUserRequestDTO APIUserRequestDTO, Integer userId);
    public void deleteUserById(Integer id);
    Page<APIUserResponseDTO> findUserWithPagination(int offset, int pageSize, String orderBy, String field);

    List<APIUserResponseDTO> findAllUsers();
    Page<APIUserResponseDTO> findUserWithPaginationAndSort(int offset, int pageSize, String field);
    public Page<APIUserResponseDTO> findPaginated(int offset, int pageSize);

    Iterable<User> saveUserList(List<User> userList);

}
