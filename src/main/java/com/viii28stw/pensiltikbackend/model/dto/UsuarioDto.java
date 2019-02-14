package com.viii28stw.pensiltikbackend.model.dto;

import com.viii28stw.pensiltikbackend.enumeration.SexoEnum;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class UsuarioDto{
    private String id;
    @NotBlank private String nome;
    @NotBlank private String sobreNome;
    @NotBlank @Email private String email;
    @NotBlank private String senha;
    private SexoEnum sexoEnum;
}
