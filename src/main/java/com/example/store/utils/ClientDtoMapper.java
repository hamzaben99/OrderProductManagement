package com.example.store.utils;

import com.example.store.entity.Client;
import com.example.store.entity.Product;
import com.example.store.entity.dto.ClientDto;
import com.example.store.entity.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoMapper {
    public ClientDto mapEntityToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmailAddress(client.getEmailAddress());
        dto.setPhoneNumber(client.getPhoneNumber());
        return dto;
    }

    public Client mapDtoToEntity(ClientDto dto) {
        return new Client(dto.getFirstName(), dto.getLastName(), dto.getPhoneNumber(), dto.getEmailAddress());
    }
}
