package integration;
import com.agibank.test.teste_tecnico.TesteTecnicoApplication;
import com.agibank.test.teste_tecnico.domain.Cliente;
import com.agibank.test.teste_tecnico.dto.ClienteDto;
import com.agibank.test.teste_tecnico.repository.ClienteRepository;
import com.agibank.test.teste_tecnico.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TesteTecnicoApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class ClienteControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    private ClienteDto clienteDto;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule()); // Suporte para LocalDate

        clienteDto = new ClienteDto(
                null,
                "Jar",
                "781.197.887-98",
                "joasso.silv@aema.cil",
                LocalDate.of(2000, 5, 15),
                "22222222222",
                "Rua das Flores, 123 - São Paulo, SP",
                null
        );
    }

    @Transactional
    @Test
    void createClienteSucess() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(clienteDto);

        mockMvc.perform(post("/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Jar"))
                .andExpect(jsonPath("$.cpf").value("781.197.887-98"));
    }

    @Transactional
    @Test
    void failToCreateCliente_cpfAlreadyExists() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(clienteDto);
        Cliente cliente = new Cliente(clienteDto);
        clienteRepository.save(cliente);

        mockMvc.perform(post("/cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Cpf já cadastrado."));
    }

    @Transactional
    @Test
    void findClienteById_sucess() throws Exception {
        Cliente cliente = new Cliente(clienteDto);
         clienteRepository.save(cliente);

        // Buscando o cliente pelo ID
        mockMvc.perform(get("/cliente/"+cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.nome").value("Jar"))
                .andExpect(jsonPath("$.cpf").value("781.197.887-98"));
    }

    @Transactional
    @Test
    void findClienteById_entityNotFound() throws Exception {
        mockMvc.perform(get("/cliente/"+99999999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado."));
    }

    @Test
    @Transactional
    void deleteCliente_success_NoContent204() throws Exception {
        Cliente cliente = clienteRepository.save(new Cliente(clienteDto));
        mockMvc.perform(delete("/cliente/" + cliente.getId()))
                .andExpect(status().isNoContent());
        Optional<Cliente> clienteNotActive = clienteRepository.findById(cliente.getId());
        assertTrue(clienteNotActive.isPresent());
        assertFalse(clienteNotActive.get().getIsActive());
    }

    }








