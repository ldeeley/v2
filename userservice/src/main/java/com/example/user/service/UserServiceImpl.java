package com.example.user.service;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.exception.UserGroupNotFoundException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.usergroup.model.UserGroup;
import com.example.usergroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    private final BannedUsersClient bannedUsersClient;

    private final ModelMapper modelMapper;


//    @Cacheable("users")
    public List<User> findAllUsers(){
        return new ArrayList<>(userRepository.findAll());
    }

//    @Cacheable(value="user",key="#p0")
public APIUserResponseDTO findUserById(Integer id) throws UserNotFoundException {

    APIUserResponseDTO apiUserResponseDTO;

    try {
        log.info("UserServiceImpl::findUserById execution started.");
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id : " + id + " not found"));
        apiUserResponseDTO = convertToDTO(user);
        log.debug("UserServiceImpl::findUserById for user {} {}", id, modelMapper.map(user, APIUserResponseDTO.class));
    } catch (Exception ex) {
        log.error("Exception occurred while reading user from database, Exception Message : {}",ex.getMessage());
        throw new UserNotFoundException("user not found with id : " + id);
    }
    log.info("UserServiceImpl::findUserById execution ended.");
    return apiUserResponseDTO;
}

    public List<APIUserResponseDTO> findAllUserWithSorting(String orderBy, String field){
        return userRepository.findAll(Sort.by( getSortDirection(orderBy), field)).stream().map(this::convertToDTO).toList();
    }

    public Page<APIUserResponseDTO> findUserWithPagination(int offset, int pageSize, String orderBy, String field){

        return userRepository.findAll(PageRequest.of(offset-1, pageSize).withSort(getSortDirection(orderBy),field)).map(this::convertToDTO);
    }

    public Page<APIUserResponseDTO> findUserWithPaginationAndSort(int offset, int pageSize, String field) {
        return userRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.by(field))).map(this::convertToDTO);
    }

    @CacheEvict(value = "users",allEntries = true)
    public void createUser(APIUserRequestDTO apiUserRequestDTO){
        User newUser = convertFromDTO(apiUserRequestDTO);
        if (!bannedUsersClient.isBannedUser(newUser.getName())) {
            userRepository.save(newUser);
            log.info("User {} is saved", newUser.getEmail());
            return;
        }
        log.info("User {} banned. Not saved", newUser.getEmail());
    }

    @Caching(evict = {
            @CacheEvict(value = "user",key = "#p0"),
            @CacheEvict(value = "users",allEntries = true)})
    public void updateUserById(APIUserRequestDTO apiUserRequestDTO, Integer userId) {
        if (!bannedUsersClient.isBannedUser(apiUserRequestDTO.getName())) {

            User user = User.builder()
                    .userId(userId)
                    .name(apiUserRequestDTO.getName())
                    .email(apiUserRequestDTO.getEmail())
                    .mobile(apiUserRequestDTO.getMobile())
                    .build();

            userRepository.save(user);
            log.info("User {} is updated", user.getEmail());
            return;
        }
        log.info("User {} banned. Not updated", userId);

    }

    @Caching(evict = {
            @CacheEvict(value = "user",key = "#p0"),
            @CacheEvict(value = "users",allEntries = true)})
    public ResponseEntity<String> deleteUserById(Integer id){
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
            log.info("User {} is deleted", id);
            return ResponseEntity.status(HttpStatus.OK).body("User is deleted");
        }

        log.info("User {} Not found. Not deleted", id);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found. Not deleted");

    }

    @Override
    public Page<APIUserResponseDTO> findPaginated(int offset, int pageSize) {
        return this.userRepository.findAll(PageRequest.of(offset,pageSize)).map(this::convertToDTO);
    }

    public Iterable<User> saveUserList(List<User> userList){
        return userRepository.saveAll(userList);
    }

    @Override
    @Transactional
    public void addUserToUserGroup(Integer userId, Integer userGroupId) throws UserNotFoundException, UserGroupNotFoundException {

        User user;
        UserGroup userGroup;

        try {
            log.info("UserServiceImpl::addUserToUserGroup execution started.");
            user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id : " + userId + " not found"));
            log.debug("UserServiceImpl::addUserToUserGroup for user {} {}", userId, modelMapper.map(user, APIUserResponseDTO.class));
        } catch (Exception ex) {
            log.error("Exception occurred while reading user from database, Exception Message : {}",ex.getMessage());
            throw new UserNotFoundException("user not found with id : " + userId);
        }

        try {
            userGroup = userGroupRepository.findById(userGroupId).orElseThrow(()-> new UserGroupNotFoundException("no usergroup found : "+userGroupId));
            log.debug("UserServiceImpl::addUserToUserGroup for user {}", userGroupId);
        } catch (Exception ex) {
            log.error("Exception occurred while reading user from database, Exception Message : {}",ex.getMessage());
            throw new UserGroupNotFoundException("userGroup not found with id : " + userGroupId);
        }

        user.addUserGroup(userGroup);
        userGroup.addUser(user);
        userRepository.save(user);
        userGroupRepository.save(userGroup);

        log.info("UserServiceImpl::addUserToUserGroup execution ended.");
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    private User convertFromDTO(APIUserRequestDTO apiUserRequestDTO){
        return modelMapper.map(apiUserRequestDTO,User.class);
    }

    private APIUserResponseDTO convertToDTO(User user){
        return modelMapper.map(user,APIUserResponseDTO.class);
    }

}
