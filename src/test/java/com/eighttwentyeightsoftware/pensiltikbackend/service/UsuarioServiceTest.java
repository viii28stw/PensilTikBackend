package com.eighttwentyeightsoftware.pensiltikbackend.service;

import com.eighttwentyeightsoftware.pensiltikbackend.enumeration.SexoEnum;
import com.eighttwentyeightsoftware.pensiltikbackend.model.dto.UsuarioDto;
import org.joda.time.DateTime;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static com.eighttwentyeightsoftware.pensiltikbackend.util.RandomValue.randomAlphabetic;
import static com.eighttwentyeightsoftware.pensiltikbackend.util.RandomValue.randomAlphanumeric;
import static org.junit.Assert.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test(expected = IllegalArgumentException.class)
    public void salvarUsuarioNaoPodeRetornarNuloEnaoDeixarSalvarDoisUsuariosComEmailJaExistente() {
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());

        assertNotNull(usuarioDto);
        usuarioService.salvarUsuario(usuarioDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void atualizarUsuarioNaoPodeRetornarNuloEOUsuarioASerAtualizadoDeveConterID() {
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());

        assertNotNull(usuarioDto);

        usuarioDto.setNome(randomAlphabetic(25));
        usuarioDto.setSobreNome(randomAlphabetic(25));
        usuarioDto.setEmail(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3));
        usuarioDto.setSenha(randomAlphanumeric(8));
        usuarioDto.setSexoEnum(SexoEnum.FEMININO);
        usuarioDto.setDataNascimento(new DateTime());

        assertNotNull(usuarioService.atualizarUsuario(usuarioDto));

        UsuarioDto usuarioDto1 = usuarioService.buscarUsuarioPorId(usuarioDto.getId());
        assertNotNull(usuarioDto1);
        assertEquals(usuarioDto1, usuarioDto);

        usuarioDto.setId(null);
        usuarioService.atualizarUsuario(usuarioDto);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void naoDeixarSalvarUsuarioSemNome() {
        usuarioService.salvarUsuario(UsuarioDto.builder()
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void naoDeixarSalvarUsuarioSemSobrenome() {
        usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void naoDeixarSalvarUsuarioSemEmail() {
        usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void naoDeixarSalvarUsuarioSemSenha() {
        usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void naoDeixarSalvarUsuarioSemSexo() {
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .dataNascimento(new DateTime())
                .build());

        usuarioService.salvarUsuario(usuarioDto);
    }

    @Test(expected = NoSuchElementException.class)
    public void buscarUsuarioPorIdNaoPodeRetornarNulo() {
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());

        assertNotNull(usuarioDto);

        UsuarioDto usuarioDto1 = usuarioService.buscarUsuarioPorId(usuarioDto.getId());

        assertNotNull(usuarioDto1);
        assertEquals(usuarioDto, usuarioDto1);

        assertTrue(usuarioService.deletarUsuarioPorId(usuarioDto.getId()));
        usuarioService.buscarUsuarioPorId(usuarioDto.getId());
    }

    @Test
    public void buscarTodosOsUsuarios(){
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());

        assertNotNull(usuarioDto);

        List<UsuarioDto> listUsuariosDto = usuarioService.buscarTodosOsUsuarios();

        assertNotNull(listUsuariosDto);
        assertFalse(listUsuariosDto.isEmpty());
        assertNotNull(listUsuariosDto.get(0));
    }

    @Test(expected = NoSuchElementException.class)
    public void deletarUsuarioPorId() {
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());

        assertNotNull(usuarioDto);

        assertTrue(usuarioService.deletarUsuarioPorId(usuarioDto.getId()));
        usuarioService.buscarUsuarioPorId(usuarioDto.getId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deletarUsuarioPorIdInexistente() {
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());

        assertNotNull(usuarioDto);

        assertTrue(usuarioService.deletarUsuarioPorId(usuarioDto.getId()));
        usuarioService.deletarUsuarioPorId(usuarioDto.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void fazerLogin() {
        UsuarioDto usuarioDto = usuarioService.salvarUsuario(UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build());

        assertNotNull(usuarioDto);

        UsuarioDto usuarioDto1 = usuarioService.fazerLogin(usuarioDto.getEmail(), usuarioDto.getSenha());

        assertNotNull(usuarioDto1);
        assertEquals(usuarioDto1, usuarioDto);

        assertTrue(usuarioService.deletarUsuarioPorId(usuarioDto.getId()));
        usuarioService.fazerLogin(usuarioDto.getEmail(), usuarioDto.getSenha());
    }

}
