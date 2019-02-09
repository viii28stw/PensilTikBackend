package com.viii28stw.pensiltikbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Plamedi L. Lusembo
 */

@AllArgsConstructor
@Getter
public enum SexoEnum {
    MASCULINO('M', "Masculino"),
    FEMININO('F', "Feminino");

    private final char id;
    private final String descricao;
}
