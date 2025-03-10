package com.agibank.test.teste_tecnico.domain;

import com.agibank.test.teste_tecnico.dto.ClienteCreateDto;
import com.agibank.test.teste_tecnico.dto.ClienteDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 letras")
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)", message = "CPF deve ser no formato XXX.XXX.XXX-XX")
    private String cpf;

    @Column(nullable = false)
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido no formato email@email.com")
    private String email;

    @Column(name = "data_nascimento", nullable = false)
    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate dataNascimento;

    @Column(length = 13)
    @Pattern(regexp = "\\d{11,13}", message = "Telefone deve ter entre 11 e 13 dígitos numéricos")
    private String telefone;

    @Column(nullable = false)
    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    @Column(nullable = false, precision = 18, scale = 2)
    @PositiveOrZero(message ="Saldo não pode ser negativo" )
    private BigDecimal saldo;

    @Column(nullable = false)
    private Boolean isActive = true;

    public Cliente(ClienteDto dto) {
        this.id =dto.id();
        this.nome = dto.nome();
        this.cpf = dto.cpf();
        this.email=dto.email();
        this.dataNascimento=dto.dataNascimento();
        this.telefone=dto.telefone();
        this.endereco=dto.endereco();
        this.saldo= (dto.saldo() != null) ? dto.saldo() : BigDecimal.ZERO;
    }
    public Cliente(ClienteCreateDto dto) {
        this.nome = dto.nome();
        this.cpf = dto.cpf();
        this.email=dto.email();
        this.dataNascimento=dto.dataNascimento();
        this.telefone=dto.telefone();
        this.endereco=dto.endereco();
    }


}
