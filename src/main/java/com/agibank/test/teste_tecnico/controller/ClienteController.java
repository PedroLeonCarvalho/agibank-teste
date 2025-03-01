package com.agibank.test.teste_tecnico.controller;

import com.agibank.test.teste_tecnico.dto.ClienteDto;
import com.agibank.test.teste_tecnico.repository.ClienteRepository;
import com.agibank.test.teste_tecnico.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    private  final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    public ClienteController (ClienteService clienteService,
                              ClienteRepository clienteRepository) {
      this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }
    
        @PostMapping
        public ResponseEntity<ClienteDto> createCliente (@RequestBody @Valid ClienteDto cliente) {
            var newCliente = clienteService.createNewCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCliente);
        }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> getClienteById (@PathVariable Long id) {
      var clienteDto = clienteService.findClienteById(id);
    return ResponseEntity.ok(clienteDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deactivateCliente (@PathVariable @Valid Long id) {
    clienteService.deleteCliente(id);
    return ResponseEntity.noContent().build();

    }
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> updateCliente(@PathVariable Long id, @RequestBody @Valid ClienteDto clienteDto) {
        ClienteDto updatedCliente = clienteService.updateCliente(id, clienteDto);
        return ResponseEntity.ok(updatedCliente);
    }



}
