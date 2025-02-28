package com.agibank.test.teste_tecnico.repository;

import com.agibank.test.teste_tecnico.domain.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {


    boolean existsByCpf(String cpf);
}
