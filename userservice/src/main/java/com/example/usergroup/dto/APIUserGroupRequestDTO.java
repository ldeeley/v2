package com.example.usergroup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class APIUserGroupRequestDTO {

    private int userGroupId;
    private String title;

}
