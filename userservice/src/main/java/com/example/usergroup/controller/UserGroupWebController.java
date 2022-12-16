package com.example.usergroup.controller;


import com.example.user.dto.APIUserResponseDTO;
import com.example.user.model.User;
import com.example.user.service.UserServiceImpl;
import com.example.usergroup.dto.APIUserGroupRequestDTO;
import com.example.usergroup.dto.APIUserGroupResponseDTO;
import com.example.usergroup.model.UserGroup;
import com.example.usergroup.service.UserGroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/usergroup")
public class UserGroupWebController {

//    @Autowired
//    UserGroupController userGroupController;
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserGroupServiceImpl userGroupService;

    public static final int pageSize = 10;
    private static final String GROUP_THYMELEAF_TEMPLATES = "/group_ThymeLeafTemplates/";

    @GetMapping("/")
     public String viewUserGroupPage(Model model){
        return findPaginated(1,model);
    }

    @GetMapping("/newUserGroupForm")
    public String newUserGroupForm(@ModelAttribute("usergroupreq") APIUserGroupRequestDTO apiUserGroupRequestDTO) {
        return GROUP_THYMELEAF_TEMPLATES+"new_usergroup";
    }

    @PostMapping("/newUserGroupForm")
    public String submitForm(@Valid @ModelAttribute("usergroupreq") APIUserGroupRequestDTO apiUserGroupRequestDTO,
                             BindingResult bindingResult,
                             RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return GROUP_THYMELEAF_TEMPLATES+"new_usergroup";
        }
        userGroupService.createUserGroup(apiUserGroupRequestDTO);
        ra.addFlashAttribute("usergroupreq", apiUserGroupRequestDTO);
        return "redirect:/usergroup/";
    }

    @GetMapping("/showUpdateMembers/{userGroupId}")
    public String updateGroupMembers(@PathVariable (value = "userGroupId") Integer userGroupId, Model model) {
        //should be optional - fix this
        APIUserGroupResponseDTO userGroupResponse = userGroupService.findUserGroupById(userGroupId).get();
        List<User> selectableUsers = userService.findAllUsers();
        List<Integer> groupMembers = userGroupService.findByuserGroupIdLike(userGroupId).stream().map(APIUserResponseDTO::getUserId).toList();
        model.addAttribute("usergroup",userGroupResponse);
        model.addAttribute("selectableUsers",selectableUsers);
        model.addAttribute("groupMembers",groupMembers);
        return "user_list_checked";
    }

    @GetMapping("/showFormForUpdate/{userGroupId}")
    public String showFormForUpdate(@PathVariable (value = "userGroupId") Integer id,Model model) {
        Optional<APIUserGroupResponseDTO> userGroupResponseDTO = userGroupService.findUserGroupById(id);
        model.addAttribute("usergroup",userGroupResponseDTO);
        return GROUP_THYMELEAF_TEMPLATES+"update_usergroup";
    }

    @PostMapping("/saveUserGroup")
    public String register(@Valid @ModelAttribute("usergroup") UserGroup userGroup, Errors errors) {
        if (!errors.hasErrors()) {
            APIUserGroupRequestDTO apiUserGroupRequestDTO = APIUserGroupRequestDTO.builder()
                    .title(userGroup.getTitle())
                    .build();
            userGroupService.updateUserGroupById(apiUserGroupRequestDTO,userGroup.getUserGroupId());
        }
        return "redirect:/usergroup/";
    }

    @GetMapping("/page/{offset}")
    public String findPaginated(@PathVariable ("offset") int pageNum, Model model ){

        Page<APIUserGroupResponseDTO> userGroupResponsePage = userGroupService.findUserGroupWithPagination(pageNum,
                                                                                    pageSize,
                                                                                    "asc",
                                                                                    "userGroupId");
        List<APIUserGroupResponseDTO> userGroupList = userGroupResponsePage.getContent();
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",userGroupResponsePage.getTotalPages());
        model.addAttribute("totalItems",userGroupResponsePage.getTotalElements());
        model.addAttribute("userGroupList",userGroupList);
        model.addAttribute("pageSize",pageSize);
        int startCount = (pageNum-1) * pageSize + 1;
        model.addAttribute("startCount",startCount);
        int endCount = startCount + pageSize -1;
        model.addAttribute("endCount",endCount);

        return GROUP_THYMELEAF_TEMPLATES+"usergroup_list";
    }
}
