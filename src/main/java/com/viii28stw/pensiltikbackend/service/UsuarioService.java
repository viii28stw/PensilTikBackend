package com.viii28stw.pensiltikbackend.service;


import com.viii28stw.pensiltikbackend.model.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    UsuarioDto buscarUsuarioPorId(String id);
    List<UsuarioDto> buscarTodosOsUsuarios();
    UsuarioDto salvarUsuario(UsuarioDto usuarioDto);
    UsuarioDto atualizarUsuario(UsuarioDto usuarioDto);
    boolean deletarUsuarioPorId(String id);
    UsuarioDto fazerLogin(String email, String senha);

}
