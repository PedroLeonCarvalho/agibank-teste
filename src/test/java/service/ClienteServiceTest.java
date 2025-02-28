package service;

import com.agibank.test.teste_tecnico.domain.Cliente;
import com.agibank.test.teste_tecnico.dto.ClienteDto;
import com.agibank.test.teste_tecnico.infra.exception.InvalidDataContentException;
import com.agibank.test.teste_tecnico.repository.ClienteRepository;
import com.agibank.test.teste_tecnico.service.ClienteService;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    Cliente clienteSalvo = new Cliente(
            1L,
            "Jar",
            "781.197.237-89",
            "joasso.silv@aema.cil",
            LocalDate.of(2000, 5, 15),
            "22222222222",
            "Rua das Flores, 123 - São Paulo, SP",
            BigDecimal.ZERO
    );

    @Test
    void createService_Success() {
        // Dado (Given)
        ClienteDto clienteDto = new ClienteDto(
                null,
                "Jar",
                "781.197.237-89",
                "joasso.silv@aema.cil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP",
                null
        );

        when(clienteRepository.existsByCpf(clienteDto.cpf())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);


        ClienteDto resultado = clienteService.createService(clienteDto);


        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Jar", resultado.nome());
        assertEquals("781.197.237-89", resultado.cpf());
        assertEquals("joasso.silv@aema.cil", resultado.email());
        assertEquals(LocalDate.of(2000, 5, 15), resultado.dataNascimento());
        assertEquals(BigDecimal.ZERO, resultado.saldo());

        verify(clienteRepository, times(1)).existsByCpf(clienteDto.cpf());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void createServiceFail_InvalidCpf() {
        ClienteDto clienteDto = new ClienteDto(
                null,
                "Jar",
                "781.197.237",
                "joasso.silv@aema.cil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP",
                null
        );

        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createService(clienteDto)
        );

        assertEquals("Formato inválido de CPF, use XXX.XXX.XXX-XX.", exception.getMessage());
    }

    @Test
    void createServiceFail_cpfAlreadyExists() {
        ClienteDto clienteDto = new ClienteDto(
                null,
                "Jazz",
                "781.197.237-00",
                "joasso.silv@aema.cil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP",
                null
        );

        when(clienteRepository.existsByCpf(clienteDto.cpf())).thenReturn(true);

        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createService(clienteDto)
        );

        assertEquals("Cpf já cadastrado.", exception.getMessage());
    }

    @Test
    void createServiceFail_InvalidEmail() {
        ClienteDto clienteDto = new ClienteDto(
                null,
                "Jar",
                "781.197.237-00",
                "joasso.silv@aemacil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP",
                null
        );

        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createService(clienteDto)
        );

        assertEquals("Email em formato inválido.", exception.getMessage());
    }

    @Test
    void createServiceFail_InvalidAge() {
        ClienteDto clienteDto = new ClienteDto(
                null,
                "Jar",
                "781.197.237-00",
                "joasso.silv@aema.cil",
                LocalDate.of(2020, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP",
                null
        );

        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createService(clienteDto)
        );

        assertEquals("A idade mínima para cadastro é de 18 anos.", exception.getMessage());
    }

    @Test
    void createServiceFail_InvalidName() {
        ClienteDto clienteDto = new ClienteDto(
                null,
                "Ja",
                "781.197.237-00",
                "joasso.silv@aema.cil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP",
                null
        );

        InvalidDataContentException exception = assertThrows(
                InvalidDataContentException.class,
                () -> clienteService.createService(clienteDto)
        );

        assertEquals("O nome deve ter no mínimo 3 letras", exception.getMessage());
    }


}
