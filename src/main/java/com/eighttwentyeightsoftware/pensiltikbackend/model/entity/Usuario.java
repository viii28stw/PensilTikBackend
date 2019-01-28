package com.eighttwentyeightsoftware.pensiltikbackend.model.entity;

import com.eighttwentyeightsoftware.pensiltikbackend.enumeration.SexoEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Entity
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(name = "IdGenerator", strategy = "com.eighttwentyeightsoftware.pensiltikbackend.util.IdGenerator")
    @GeneratedValue(generator = "IdGenerator")
    @Id
    @Column(name = "ID", length = 25)
    private String id;

    @Column(name = "NOME", length = 25, nullable = false)
    private String nome;

    @Column(name = "SOBRE_NOME", length = 25, nullable = false)
    private String sobreNome;

    @Email
    @Column(name = "EMAIL", length = 25, nullable = false)
    private String email;

    @Column(name = "SENHA", length = 8, nullable = false)
    private String senha;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SEXO", nullable = false)
    private SexoEnum sexoEnum;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    private DateTime dataNascimento;


}
