package com.example.usergroup.service;

import com.example.user.repository.UserRepository;
import com.example.user.service.BannedUsersClient;
import com.example.user.utils.Utils;
import com.example.usergroup.model.UserGroup;
import com.example.usergroup.repository.UserGroupRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserGroupServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private BannedUsersClient bannedUsersClient;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<UserGroup> userGroupArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<UserGroup>> listUserGroupArgumentCaptor;

    @InjectMocks
    private UserGroupServiceImpl userGroupService;

    @Test
    void findAllUserGroups() {
        Mockito.when(userGroupRepository.findAll()).thenReturn(Utils.createUserGroupList());

        assertEquals(3,userGroupService.findAllUserGroups().size());

    }

    @Test
    @Disabled("later")
    void findAllUserGroupsWithSorting() {
    }

    @Test
    void updateUserGroupById() {

        //cut
        userGroupService.updateUserGroupById(Utils.CreateAPIUserGroupRequestDTO("discount"),1);

        verify(userGroupRepository,times(1)).save(userGroupArgumentCaptor.capture());
        assertThat(userGroupArgumentCaptor.getValue().getTitle()).isEqualTo("discount");

    }

    @Disabled("later")
    @Test
    void deleteUserGroupById() {

    }

    @DisplayName("Check that creating a UserGroup returns an OK status code and the URL of the new UserGroup created")
    @Test
    void createUserGroup() {

        //cut
        userGroupService.createUserGroup(Utils.CreateAPIUserGroupRequestDTO("premium"));

        verify(userGroupRepository,times(1)).save(userGroupArgumentCaptor.capture());
        assertThat(userGroupArgumentCaptor.getValue().getTitle()).isEqualTo("premium");
    }

    @Test
    void findUserGroupById() {

        Mockito.when(userGroupRepository.findById(1)).thenReturn(Optional.of(Utils.CreateUserGroup(1)));

        userGroupService.createUserGroup(Utils.CreateAPIUserGroupRequestDTO("premium"));
        verify(userGroupRepository,times(1)).save(userGroupArgumentCaptor.capture());
        assertThat(userGroupArgumentCaptor.getValue().getTitle()).isEqualTo("premium");
    }

    @Test
    @Disabled("later")
    void findByuserGroupIdLike() {
    }

    @Test
    @Disabled("later")
    void saveUserGroup() {
    }

    @Test
    @Disabled("later")
    void findUserGroupWithPagination() {
    }
}