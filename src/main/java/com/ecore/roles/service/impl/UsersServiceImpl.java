package com.ecore.roles.service.impl;

import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersClient usersClient;

    @Override
    public User getUser(UUID id) {
        return usersClient.getUser(id).getBody();
    }

    @Override
    public List<User> getUsers() {
        return usersClient.getUsers().getBody();
    }
}
