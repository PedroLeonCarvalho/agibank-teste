package com.agibank.test.teste_tecnico.controller;

import com.agibank.test.teste_tecnico.dto.ClienteCreateDto;
import com.agibank.test.teste_tecnico.dto.ClienteDto;
import com.agibank.test.teste_tecnico.repository.ClienteRepository;
import com.agibank.test.teste_tecnico.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Cliente", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {
    private  final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    public ClienteController (ClienteService clienteService,
                              ClienteRepository clienteRepository) {
      this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

        @Operation(summary = "Cria um novo cliente", description = "Recebe os dados do cliente e cadastra no sistema.")
        @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
        @PostMapping
        public ResponseEntity<ClienteDto> createCliente (@RequestBody @Valid ClienteCreateDto cliente) {
            var newCliente = clienteService.createNewCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCliente);
        }

    @Operation(summary = "Busca um cliente pelo ID", description = "Retorna os dados do cliente correspondente ao ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> getClienteById (@PathVariable Long id) {
      var clienteDto = clienteService.findClienteById(id);
    return ResponseEntity.ok(clienteDto);

    }

    @Operation(summary = "Desativa um cliente", description = "Marca um cliente como inativo no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente desativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deactivateCliente (@PathVariable @Valid Long id) {
    clienteService.deleteCliente(id);
    return ResponseEntity.noContent().build();

    }
    @Operation(summary = "Atualiza os dados de um cliente", description = "Modifica as informações de um cliente existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> updateCliente(@PathVariable Long id, @RequestBody @Valid ClienteCreateDto clienteDto) {
        ClienteDto updatedCliente = clienteService.updateCliente(id, clienteDto);
        return ResponseEntity.ok(updatedCliente);
    }



}
