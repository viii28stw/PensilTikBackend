package com.eighttwentyeightsoftware.pensiltikbackend.service;

import com.eighttwentyeightsoftware.pensiltikbackend.model.dto.UsuarioDto;
import com.eighttwentyeightsoftware.pensiltikbackend.model.entity.Usuario;
import com.eighttwentyeightsoftware.pensiltikbackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UsuarioDto buscarUsuarioPorId(String id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if(usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
            return UsuarioDto.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .sobreNome(usuario.getSobreNome())
                    .email(usuario.getEmail())
                    .senha(usuario.getSenha())
                    .sexoEnum(usuario.getSexoEnum())
                    .dataNascimento(usuario.getDataNascimento())
                    .build();

        } else throw new NoSuchElementException("Não existe usuário com o ID informado");
    }

    @Override
    public List<UsuarioDto> buscarTodosOsUsuarios(){
        List<UsuarioDto> usuariosDto = new ArrayList();
        for(Usuario usuario : usuarioRepository.findAll()) {
            usuariosDto.add(UsuarioDto.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .sobreNome(usuario.getSobreNome())
                    .email(usuario.getEmail())
                    .senha(usuario.getSenha())
                    .sexoEnum(usuario.getSexoEnum())
                    .dataNascimento(usuario.getDataNascimento())
                    .build());
        }
        return usuariosDto;
    }

    @Override
    public UsuarioDto salvarUsuario(UsuarioDto usuarioDto) {
        if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
                throw new IllegalArgumentException("Este e-mail já existe");
        }

        return persistir(usuarioDto);
    }

    @Override
    public UsuarioDto atualizarUsuario(UsuarioDto usuarioDto) {
        if (usuarioDto.getId() == null || usuarioDto.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("O usuário informado não contem ID");
        }

        return persistir(usuarioDto);
    }

    private UsuarioDto persistir(UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .id(usuarioDto.getId())
                .nome(usuarioDto.getNome())
                .sobreNome(usuarioDto.getSobreNome())
                .email(usuarioDto.getEmail())
                .senha(usuarioDto.getSenha())
                .sexoEnum(usuarioDto.getSexoEnum())
                .dataNascimento(usuarioDto.getDataNascimento())
                .build());

        return UsuarioDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .sobreNome(usuario.getSobreNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .sexoEnum(usuario.getSexoEnum())
                .dataNascimento(usuario.getDataNascimento())
                .build();
    }

    @Override
    public boolean deletarUsuarioPorId(String id){
        usuarioRepository.deleteById(id);
        return true;
    }

    @Override
    public UsuarioDto fazerLogin(String email, String senha){
        if(usuarioRepository.findByEmailAndSenha(email, senha) == null) {
            throw new NoSuchElementException("E-mail ou senha incorreta");
        }

        Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);

        return UsuarioDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .sobreNome(usuario.getSobreNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .sexoEnum(usuario.getSexoEnum())
                .dataNascimento(usuario.getDataNascimento())
                .build();
    }

}
