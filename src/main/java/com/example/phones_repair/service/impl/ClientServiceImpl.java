package com.example.phones_repair.service.impl;

import com.example.phones_repair.dto.user.ClientRegisterRequest;
import com.example.phones_repair.entities.Client;
import com.example.phones_repair.repositories.ClientRepository;
import com.example.phones_repair.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public void registerClient(ClientRegisterRequest registerRequest) {
        Client client = new Client();
        client.setEmail(registerRequest.getEmail());
        client.setFullName(registerRequest.getFullName());
        client.setPhoneNumber(registerRequest.getPhoneNumber());
        clientRepository.save(client);
    }

    @Override
    public List<Client> findClient(String email) {
        return clientRepository.findClientsByEmail(email);
    }
}
