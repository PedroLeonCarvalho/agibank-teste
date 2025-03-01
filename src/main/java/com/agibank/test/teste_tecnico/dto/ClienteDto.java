package com.agibank.test.teste_tecnico.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteDto(

        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, message = "O nome deve ter no mínimo 3 letras")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)", message = "CPF deve ser no formato XXX.XXX.XXX-XX")
        String cpf,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "O e-mail deve estar no formato nome@email.com")
        String email,

        @NotNull(message = "Data de nascimento é obrigatória")
        LocalDate dataNascimento,

        @Pattern(regexp = "\\d{11,13}", message = "Telefone deve ter entre 11 e 13 dígitos numéricos")
        String telefone,

        @NotBlank(message = "Endereço é obrigatório")
        String endereco,

        @PositiveOrZero
        BigDecimal saldo
) {}
