package com.eighttwentyeightsoftware.pensiltikbackend.repository;

import com.eighttwentyeightsoftware.pensiltikbackend.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    boolean existsByEmail(String email);
    Usuario findByEmailAndSenha(String email, String senha);
}