package com.example.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BannedUsersClient {

    public boolean isBannedUser(String username){
        System.out.println("Checking if "+username+" is banned");
        return username.equalsIgnoreCase("Lester");
    }

}
