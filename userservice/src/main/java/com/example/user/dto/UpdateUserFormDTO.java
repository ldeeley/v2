package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateUserFormDTO {


    int userId;
    @NotNull(message = "id cannot be null")
    @Size(min = 2, max = 10, message = "length should be in between 2 to 10")
    private String name;
    @NotEmpty(message = "Email field should not be empty")
    @NotBlank(message = "no blanks allowed")
    @Email(regexp = "^(.+)@(.+)$", message = "Invalid email pattern")
    private String email;
    @Pattern(regexp = "[7-9]\\d{9}", message = "invalid mobile number.")
    @Size(max = 10, message = "digits should be 10")
    private String mobile;

}
