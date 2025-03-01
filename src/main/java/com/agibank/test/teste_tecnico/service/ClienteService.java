package com.agibank.test.teste_tecnico.service;

import com.agibank.test.teste_tecnico.domain.Cliente;
import com.agibank.test.teste_tecnico.dto.ClienteCreateDto;
import com.agibank.test.teste_tecnico.dto.ClienteDto;
import com.agibank.test.teste_tecnico.infra.exception.InvalidDataContentException;
import com.agibank.test.teste_tecnico.repository.ClienteRepository;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteDto createNewCliente(ClienteCreateDto dto) {

        if(repository.existsByCpf(dto.cpf())){
            throw new InvalidDataContentException("Cpf já cadastrado.");
        };
        if (!isValidCpf(dto.cpf())){
            throw new InvalidDataContentException("Formato inválido de CPF, use XXX.XXX.XXX-XX.");
        }
        if(!isValidEmail(dto.email())){
            throw new InvalidDataContentException("Email em formato inválido.");
        };
        Long idade = ChronoUnit.YEARS.between(dto.dataNascimento(), LocalDate.now());
        if(idade < 18) {
            throw new InvalidDataContentException("A idade mínima para cadastro é de 18 anos.");
        }

        if(dto.nome().length() < 3) {
            throw new InvalidDataContentException("O nome deve ter no mínimo 3 letras");
        }


        Cliente cliente = new Cliente(dto);
        if(cliente.getSaldo() ==null) {
            cliente.setSaldo(BigDecimal.ZERO);
        };

        cliente = repository.save(cliente);
        return convertToDto(cliente);

    }

    private boolean isValidEmail (String email)  {
        String emailRegex = "^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        if (email == null || email.length() > 320) {
            return false;
        }

        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidCpf (String cpf)  {
        String cpfRegex = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)";
        Pattern cpfPattern = Pattern.compile(cpfRegex);

        if (cpf == null) {
            return false;
        }
        Matcher matcher = cpfPattern.matcher(cpf);
        return matcher.matches();
    }

    public ClienteDto convertToDto (Cliente cliente) {
       return  new ClienteDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getDataNascimento(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getSaldo()
        );
    }

    public ClienteDto findClienteById(Long id) {
      var cliente = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));;
        if(cliente.getIsActive() == false){
            throw new EntityNotFoundException("Cliente não ativo.");
        }
      return convertToDto(cliente);
    }

    public void deleteCliente(@Valid Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
    cliente.setIsActive(false);
    repository.save(cliente);
    }

    public ClienteDto updateCliente(Long id, ClienteCreateDto clienteDto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        if (cliente.getIsActive()==false){
            throw new EntityNotFoundException("Cliente não está ativo.");
        }
        cliente.setNome(clienteDto.nome());
        cliente.setCpf(clienteDto.cpf());
        cliente.setEndereco(clienteDto.endereco());
        cliente.setTelefone(clienteDto.telefone());
        cliente.setDataNascimento(clienteDto.dataNascimento());
        repository.save(cliente);
        return convertToDto(cliente);
    }
}
