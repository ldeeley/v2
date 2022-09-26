package com.example.user.service;

import com.example.user.dto.APIUserRequestDTO;
import com.example.user.exception.UserGroupNotFoundException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.usergroup.model.UserGroup;
import com.example.usergroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    private final BannedUsersClient bannedUsersClient;

    @Cacheable("users")
    public List<User> findAllUsers(){
        List<User> userList = new ArrayList<>();
        userList.addAll(userRepository.findAll());
        return userList;
    }

    @Cacheable(value="user",key="#p0")
    public User findUserById(Integer id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("user not found with id : "+id);
        }
    }

    public List<User> findAllUserWithSorting(String orderBy, String field){
        List<User> userList = new ArrayList<>();
        userList.addAll(userRepository.findAll(Sort.by( getSortDirection(orderBy), field)));
        return userList;
    }

    public Page<User> findUserWithPagination(int offset, int pageSize, String orderBy, String field){

        return userRepository.findAll(PageRequest.of(offset-1, pageSize).withSort(getSortDirection(orderBy),field));
    }

    public Page<User> findUserWithPaginationAndSort(int offset, int pageSize, String field) {
        return userRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.by(field)));
    }

    @CacheEvict(value = "users",allEntries = true)
    public ResponseEntity<String> createUser(User newUser){

        if (!bannedUsersClient.isBannedUser(newUser.getName())) {

            userRepository.save(newUser);
            log.info("User {} is saved", newUser.getEmail());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(URI.create("/rnr/user/users/"+newUser.getUserId()));
            return new ResponseEntity<>(responseHeaders,HttpStatus.OK);
        }
        log.info("User {} banned. Not saved", newUser.getEmail());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is banned");
    }

    @Caching(evict = {
            @CacheEvict(value = "user",key = "#p0"),
            @CacheEvict(value = "users",allEntries = true)})
    public void updateUserById(APIUserRequestDTO APIUserRequestDTO, Integer userId) {
        if (!bannedUsersClient.isBannedUser(APIUserRequestDTO.getName())) {

            User user = User.builder()
                    .userId(userId)
                    .name(APIUserRequestDTO.getName())
                    .email(APIUserRequestDTO.getEmail())
                    .mobile(APIUserRequestDTO.getMobile())
                    .build();

            userRepository.save(user);
            log.info("User {} is saved", user.getEmail());
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "user",key = "#p0"),
            @CacheEvict(value = "users",allEntries = true)})
    public void deleteUserById(Integer id){
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> findPaginated(int offset, int pageSize) {
        return this.userRepository.findAll(PageRequest.of(offset,pageSize));
    }



    public Iterable<User> saveUserList(List<User> userList){
        return userRepository.saveAll(userList);
    }

    @Override
    @Transactional
    public void addUserToUserGroup(Integer userId, Integer userGroupId) throws UserNotFoundException, UserGroupNotFoundException {
        User testUser = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("no user found : "+userId));
        UserGroup userGroup = userGroupRepository.findById(userGroupId).orElseThrow(()-> new UserGroupNotFoundException("no usergroup found : "+userGroupId));
        testUser.addUserGroup(userGroup);
        userGroup.addUser(testUser);
        userRepository.save(testUser);
        userGroupRepository.save(userGroup);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

}
