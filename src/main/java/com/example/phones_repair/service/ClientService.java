package com.example.phones_repair.service;

import com.example.phones_repair.dto.user.ClientRegisterRequest;
import com.example.phones_repair.entities.Client;

import java.util.List;

public interface ClientService {

    String registerClient(ClientRegisterRequest registerRequest);
    List<Client> findClient(String email);
}
