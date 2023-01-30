package com.ecore.roles.service.impl;

import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.UsersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersClient usersClient;

    public UsersServiceImpl(UsersClient usersClient) {
        this.usersClient = usersClient;
    }

    public User getUser(UUID id) {
        return ofNullable(usersClient.getUser(id).getBody())
                .orElseThrow(() -> new ResourceNotFoundException(User.class, id));
    }

    public List<User> getUsers() {
        return usersClient.getUsers().getBody();
    }
}
