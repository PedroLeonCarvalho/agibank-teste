package service;

import com.agibank.test.teste_tecnico.domain.Cliente;
import com.agibank.test.teste_tecnico.dto.ClienteCreateDto;
import com.agibank.test.teste_tecnico.infra.exception.InvalidDataContentException;
import com.agibank.test.teste_tecnico.repository.ClienteRepository;
import com.agibank.test.teste_tecnico.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;
    
    Cliente savedCliente = new Cliente(
            1L,
            "Jar",
            "781.197.237-89",
            "joasso.silv@aema.cil",
            LocalDate.of(2000, 5, 15),
            "22222222222",
            "Rua das Flores, 123 - São Paulo, SP",
            BigDecimal.ZERO,
            true
    );
    
    ClienteCreateDto clienteDto = new ClienteCreateDto(

            "Jar",
            "781.197.237-89",
            "joasso.silv@aema.cil",
            LocalDate.of(2000, 5, 15),
            "22222222222",
            "Rua das Flores, 123 - São Paulo, SP"
    );
    
    @Test
    void createNewCliente_Success() {
        when(clienteRepository.existsByCpf(clienteDto.cpf())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(savedCliente);
        var dto = clienteService.createNewCliente(clienteDto);
        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("Jar", dto.nome());
        assertEquals("781.197.237-89", dto.cpf());
        assertEquals("joasso.silv@aema.cil", dto.email());
        assertEquals(LocalDate.of(2000, 5, 15), dto.dataNascimento());
        assertEquals(BigDecimal.ZERO, dto.saldo());
        verify(clienteRepository, times(1)).existsByCpf(clienteDto.cpf());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void createNewClienteFail_InvalidCpf() {
        ClienteCreateDto clienteDto = new ClienteCreateDto(
                "Jar",
                "781.197.237",
                "joasso.silv@aema.cil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP"
        );
        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createNewCliente(clienteDto)
        );
        assertEquals("Formato inválido de CPF, use XXX.XXX.XXX-XX.", exception.getMessage());
    }

    @Test
    void createNewClienteFail_cpfAlreadyExists() {
        when(clienteRepository.existsByCpf(clienteDto.cpf())).thenReturn(true);
        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createNewCliente(clienteDto)
        );
        assertEquals("Cpf já cadastrado.", exception.getMessage());
    }

    @Test
    void createNewClienteFail_InvalidEmail() {
        ClienteCreateDto clienteDto = new ClienteCreateDto(
                "Jar",
                "781.197.237-89",
                "joasso.silv@aema",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP"
        );
        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createNewCliente(clienteDto)
        );
        assertEquals("Email em formato inválido.", exception.getMessage());
    }

    @Test
    void createNewClienteFail_InvalidAge() {
        ClienteCreateDto clienteDto = new ClienteCreateDto(
                "Jar",
                "781.197.237-89",
                "joasso.silv@aema.cil",
                LocalDate.of(2020, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP"
        );
        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createNewCliente(clienteDto)
        );
        assertEquals("A idade mínima para cadastro é de 18 anos.", exception.getMessage());
    }

    @Test
    void createNewClienteFail_InvalidName() {
        ClienteCreateDto clienteDto = new ClienteCreateDto(
                "J",
                "781.197.237-89",
                "joasso.silv@aema.cil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP"
        );
        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createNewCliente(clienteDto)
        );
        assertEquals("O nome deve ter no mínimo 3 letras", exception.getMessage());
    }

    @Test
    void findClientById_Sucess (){
        when(clienteRepository.findById(any())).thenReturn(Optional.of(savedCliente));
        var result = clienteService.findClienteById(1L);
        assertNotNull(result);
        assertEquals(savedCliente.getId(), result.id());
        assertEquals(savedCliente.getNome(), result.nome());
        assertEquals(savedCliente.getEndereco(), result.endereco());
        assertEquals(savedCliente.getCpf(), result.cpf());
        assertEquals(savedCliente.getEmail(), result.email());
        assertEquals(savedCliente.getSaldo(), result.saldo());
        assertEquals(savedCliente.getTelefone(), result.telefone());
    }
    
    @Test
    void findClientById_fail_isAtctiveFalse (){
        savedCliente.setIsActive(false);
        when(clienteRepository.findById(any())).thenReturn(Optional.of(savedCliente));
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clienteService.findClienteById(1L));
        assertEquals("Cliente não ativo.", exception.getMessage());
    }

    @Test
    void findClientById_EntityNotFound (){
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clienteService.findClienteById(1L));
        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
    }
    
    @Test
    void deleteCliente_sucess_NoContent204 (){
        when(clienteRepository.findById(any())).thenReturn(Optional.of(savedCliente));
        clienteService.deleteCliente(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void updateCliente_Sucess() {
        Cliente cliente = new Cliente(clienteDto);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        var updatedCliente = clienteService.updateCliente(1L, clienteDto);
        assertNotNull(updatedCliente);
        assertEquals(clienteDto.nome(), updatedCliente.nome());
        assertEquals(clienteDto.cpf(), updatedCliente.cpf());
        assertEquals(clienteDto.email(), updatedCliente.email());
        assertEquals(clienteDto.telefone(), updatedCliente.telefone());
        assertEquals(clienteDto.endereco(), updatedCliente.endereco());
    }

    @Test
    void updateCliente_Fail() {
        ClienteCreateDto clienteDtoDiferentData = new ClienteCreateDto(
                "Jazzz",
                "781.197.22-00",
                "joasso.silv@aem.cil",
                LocalDate.of(2000, 5, 15),
                "22222422222",
                "Rua das pedras, 123 - São Paulo, SP"
        );
        Cliente cliente = new Cliente(clienteDto);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        var updatedCliente = clienteService.updateCliente(1L, clienteDto);
        assertNotNull(updatedCliente);
        assertNotEquals(clienteDtoDiferentData.nome(), updatedCliente.nome());
        assertNotEquals(clienteDtoDiferentData.cpf(), updatedCliente.cpf());
        assertNotEquals(clienteDtoDiferentData.email(), updatedCliente.email());
        assertNotEquals(clienteDtoDiferentData.telefone(), updatedCliente.telefone());
        assertNotEquals(clienteDtoDiferentData.endereco(), updatedCliente.endereco());
    }
    
}
