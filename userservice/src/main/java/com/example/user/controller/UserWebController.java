package com.example.user.controller;


import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.dto.UpdateUserFormDTO;
import com.example.user.exception.UserNotFoundException;
import com.example.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserWebController {

    @Autowired
    UserServiceImpl userService;

    @GetMapping("/")
    public String viewUserPage(Model model){
        return findPaginated(1,model);
    }

    public static final int PAGE_SIZE = 10;
    private static final String USER_HOME = "redirect:/user/";
    private static final String USER_THYMELEAF_TEMPLATES = "/user_ThymeLeafTemplates/";

    @GetMapping("/newUserForm")
    public String newUserForm(@ModelAttribute("userreq") APIUserRequestDTO apiUserRequestDTO) {
        return USER_THYMELEAF_TEMPLATES+"new_user";
    }

    @PostMapping("/newUserForm")
    public String submitForm(@Valid @ModelAttribute("userreq") APIUserRequestDTO apiUserRequestDTO,
                             BindingResult bindingResult,
                             RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return USER_THYMELEAF_TEMPLATES+"new_user";
        }
        userService.createUser(apiUserRequestDTO);
        ra.addFlashAttribute("userreq", apiUserRequestDTO);
        return USER_HOME;
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable (value = "id") Integer id,Model model) throws UserNotFoundException {
        try {
            APIUserResponseDTO userResponse = userService.findUserById(id);
            model.addAttribute("userdto",userResponse);
            model.addAttribute("id",id);
            return USER_THYMELEAF_TEMPLATES+"update_user";
        } catch (UserNotFoundException e){
            throw new UserNotFoundException("User not found in database : "+id);
        }
    }

    @PostMapping("/saveUser")
    public String register(@Valid @ModelAttribute("userdto") UpdateUserFormDTO updateUserFormDTO,
                           BindingResult bindingResult,
                           RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
          return USER_THYMELEAF_TEMPLATES+"update_user";
        }

            APIUserRequestDTO apiUserRequestDTO = APIUserRequestDTO.builder()
                    .name(updateUserFormDTO.getName())
                    .mobile(updateUserFormDTO.getMobile())
                    .email(updateUserFormDTO.getEmail())
                    .build();
            userService.updateUserById(apiUserRequestDTO, updateUserFormDTO.getUserId());
        ra.addFlashAttribute("userreq", apiUserRequestDTO);
        return USER_HOME;
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUserById(@PathVariable Integer userId){
        userService.deleteUserById(userId);
        return USER_HOME;
    }

    @GetMapping("/page/{offset}")
    public String findPaginated(@PathVariable ("offset") int pageNum, Model model ){

        Page<APIUserResponseDTO> userResponsePage = userService.findUserWithPagination(pageNum,
                PAGE_SIZE,
                                                                                    "asc",
                                                                                    "userId");
        List<APIUserResponseDTO> userList = userResponsePage.getContent();
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",userResponsePage.getTotalPages());
        model.addAttribute("totalItems",userResponsePage.getTotalElements());
        model.addAttribute("userList",userList);
        model.addAttribute("pageSize", PAGE_SIZE);
        int startCount = (pageNum-1) * PAGE_SIZE + 1;
        model.addAttribute("startCount",startCount);
        int endCount = startCount + PAGE_SIZE -1;
        model.addAttribute("endCount",endCount);

        return USER_THYMELEAF_TEMPLATES+"user_list";
    }

}
