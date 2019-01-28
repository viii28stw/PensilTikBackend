package com.eighttwentyeightsoftware.pensiltikbackend.model.dto;

import com.eighttwentyeightsoftware.pensiltikbackend.enumeration.SexoEnum;
import lombok.*;
import org.joda.time.DateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class UsuarioDto{

    private String id;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobreNome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    private SexoEnum sexoEnum;

    private DateTime dataNascimento;
}
