package com.example.user.controller;


import com.example.user.dto.APIUserRequestDTO;
import com.example.user.dto.APIUserResponseDTO;
import com.example.user.exception.UserNotFoundException;
import com.example.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserWebController {

    @Autowired
    UserController userController;

    @GetMapping("/")
    public String viewUserPage(Model model){
        return findPaginated(1,model);
    }

    public static final int pageSize = 10;

    @GetMapping("/newUserForm")
    public String newUserForm(@ModelAttribute("userreq") APIUserRequestDTO APIUserRequestDTO) {
        return "new_user";
    }

    @PostMapping("/newUserForm")
    public String submitForm(@Valid @ModelAttribute("userreq") APIUserRequestDTO APIUserRequestDTO,
                             BindingResult bindingResult,
                             RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "new_user";
        }
        userController.createUser(APIUserRequestDTO);
        ra.addFlashAttribute("userreq", APIUserRequestDTO);
        return "redirect:/user/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable (value = "id") Integer id,Model model) throws UserNotFoundException {
        ResponseEntity<APIUserResponseDTO> userResponse = userController.findUserResponseById(id);
        model.addAttribute("user",userResponse.getBody());
        return "update_user";
    }

    @PostMapping("/saveUser")
    public String register(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (!errors.hasErrors()) {
            APIUserRequestDTO APIUserRequestDTO = APIUserRequestDTO.builder()
                    .name(user.getName())
                    .mobile(user.getMobile())
                    .email(user.getEmail()).build();
            userController.updateUserById(user.getUserId(), APIUserRequestDTO);
        }
        return "redirect:/user/";
    }


    @GetMapping("/deleteUser/{userId}")
    public String deleteUserById(@PathVariable Integer userId){
        userController.deleteUserById(userId);
        return "redirect:/user/";
    }

    @GetMapping("/page/{offset}")
    public String findPaginated(@PathVariable ("offset") int pageNum, Model model ){

        Page<APIUserResponseDTO> userResponsePage = userController.getUserWithOffSetPageSize(pageNum,
                                                                                    pageSize,
                                                                                    "asc",
                                                                                    "userId");
        List<APIUserResponseDTO> userList = userResponsePage.getContent();
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPages",userResponsePage.getTotalPages());
        model.addAttribute("totalItems",userResponsePage.getTotalElements());
        model.addAttribute("userList",userList);
        model.addAttribute("pageSize",pageSize);
        int startCount = (pageNum-1) * pageSize + 1;
        model.addAttribute("startCount",startCount);
        int endCount = startCount + pageSize -1;
        model.addAttribute("endCount",endCount);

        return "user_list";
    }



}
