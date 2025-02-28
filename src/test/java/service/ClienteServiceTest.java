package service;

import com.agibank.test.teste_tecnico.domain.Cliente;
import com.agibank.test.teste_tecnico.dto.ClienteDto;
import com.agibank.test.teste_tecnico.repository.ClienteRepository;
import com.agibank.test.teste_tecnico.service.ClienteService;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class ClienteServiceTest {


    private final ClienteService clienteService;

    @Mock
    private final ClienteRepository repository;

    public ClienteServiceTest(ClienteService clienteService, ClienteRepository repository) {
        this.clienteService = clienteService;
        this.repository = repository;
    }


    @Test
    void createCustomerSucess() {

        ClienteDto cliente = new ClienteDto(
                "Jar",
                "781197237",
                "joasso.silv@aema.cil",
                  LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP"
        );

    }
}
