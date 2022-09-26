package com.example.usergroup.controller;


import com.example.usergroup.dto.UserGroupDTOResponse;
import com.example.usergroup.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.usergroup.dto.UserGroupRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/usergroup")
public class UserGroupWebController {

    @Autowired
    UserGroupController userGroupController;

    @GetMapping("/")
    public String viewUserGroupPage(Model model){
        return findPaginated(1,model);
    }

    public static final int pageSize = 10;

    @GetMapping("/newUserGroupForm")
    public String newUserGroupForm(@ModelAttribute("usergroupreq") UserGroupRequest userGroupRequest) {
        return "new_usergroup";
    }

    @PostMapping("/newUserGroupForm")
    public String submitForm(@Valid @ModelAttribute("usergroupreq") UserGroupRequest userGroupRequest,
                             BindingResult bindingResult,
                             RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "new_usergroup";
        }
        userGroupController.createUserGroup(userGroupRequest);
        ra.addFlashAttribute("usergroupreq", userGroupRequest);
        return "redirect:/usergroup/";
    }

//    @GetMapping("/getUserById")
//    public String getUserById(Model model) {
////        model.addAttribute("users",userService.getAllUsers());
//        return "index";
//    }

//    @PutMapping("/updateUser")
//    public String updateUser(@ModelAttribute("usergroup") UserGroupRequest userRequest){
//        userGroupController.updateUserGroupById(id, userRequest);
//        return "redirect:/getAllUsers";
//    }

    @GetMapping("/showFormForUpdate/{userGroupId}")
    public String showFormForUpdate(@PathVariable (value = "userGroupId") Integer id,Model model) {
        Optional<UserGroupDTOResponse> userGroupResponse = userGroupController.findUserGroupResponseById(id);
        model.addAttribute("usergroup",userGroupResponse);
        return "update_usergroup";
    }

    @PostMapping("/saveUserGroup")
    public String register(@Valid @ModelAttribute("usergroup") UserGroup userGroup, Errors errors) {
        if (!errors.hasErrors()) {
            UserGroupRequest userGroupRequest = UserGroupRequest.builder()
                    .title(userGroup.getTitle())
                    .build();
            userGroupController.updateUserGroupById(userGroup.getUserGroupId(), userGroupRequest);
        }
        return "redirect:/usergroup/";

    }

    @GetMapping("/page/{offset}")
    public String findPaginated(@PathVariable ("offset") int pageNum, Model model ){

        Page<UserGroupDTOResponse> userGroupResponsePage = userGroupController.getUserGroupWithOffSetPageSize(pageNum,
                                                                                    pageSize,
                                                                                    "asc",
                                                                                    "userGroupId");
        List<UserGroupDTOResponse> userGroupList = userGroupResponsePage.getContent();
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",userGroupResponsePage.getTotalPages());
        model.addAttribute("totalItems",userGroupResponsePage.getTotalElements());
        model.addAttribute("userGroupList",userGroupList);
        model.addAttribute("pageSize",pageSize);
        int startCount = (pageNum-1) * pageSize + 1;
        model.addAttribute("startCount",startCount);
        int endCount = startCount + pageSize -1;
        model.addAttribute("endCount",endCount);

        return "usergroup_list";
    }
}
