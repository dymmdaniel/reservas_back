package com.netlogistik.reservas_back.service;

import com.netlogistik.reservas_back.model.User;

import java.util.ArrayList;

public interface UserService {
    public ArrayList<User> listUsers();

    public User save(User user);

    public void delete(Long id);

    public User find(Long id);

    public User findByEmail(String email);
}
