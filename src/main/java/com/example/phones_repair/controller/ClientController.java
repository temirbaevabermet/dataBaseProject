package com.example.phones_repair.controller;


import com.example.phones_repair.dto.user.ClientRegisterRequest;
import com.example.phones_repair.entities.Client;
import com.example.phones_repair.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/add")
    public void addClient(@RequestBody ClientRegisterRequest registerRequest){
        clientService.registerClient(registerRequest);
    }

    @GetMapping("/findByEmail")
    public List<Client> findClient(@RequestParam String email){
        return clientService.findClient(email);
    }
}