package com.agibank.test.teste_tecnico.controller;

import com.agibank.test.teste_tecnico.dto.ClienteDto;
import com.agibank.test.teste_tecnico.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    private  final ClienteService clienteService;
    
  public ClienteController (ClienteService clienteService) {
      this.clienteService = clienteService;
  }
    
        @PostMapping
       //@Operation(summary = "Criar novo usuário", description = "Cria um novo usuário e retorna os dados cadastrados.")
        public ResponseEntity<ClienteDto> createUser (@RequestBody @Valid ClienteDto cliente) {
            var newCliente = clienteService.createService(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCliente);
        }

}
