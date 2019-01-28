package com.eighttwentyeightsoftware.pensiltikbackend.controller;


import com.eighttwentyeightsoftware.pensiltikbackend.enumeration.SexoEnum;
import com.eighttwentyeightsoftware.pensiltikbackend.model.dto.UsuarioDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import com.eighttwentyeightsoftware.pensiltikbackend.util.UrlPrefixFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static com.eighttwentyeightsoftware.pensiltikbackend.util.RandomValue.randomAlphabetic;
import static com.eighttwentyeightsoftware.pensiltikbackend.util.RandomValue.randomAlphanumeric;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private HttpHeaders httpHeaders;

    private static final String BUSCAR_USUARIO_POR_ID = "/buscarusuarioporid/";
    private static final String BUSCAR_TODOS_OS_USUARIOS = "/buscartodososusuarios/";
    private static final String SALVAR_USUARIO = "/salvarusuario/";
    private static final String ATUALIZAR_USUARIO = "/atualizarusuario/";
    private static final String DELETAR_USUARIO_POR_ID = "/deletarusuarioporid/";
    private static final String FAZER_LOGIN = "/fazerlogin/";
    
    
    @Test
    public void salvarUsuarioNaoPodeRetornarNuloEnaoDeixarSalvarDoisUsuariosComEmailJaExistente() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof UsuarioDto);

        ResponseEntity responseEntityUsuario1 = testRestTemplate.exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario1.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertNotNull(responseEntityUsuario1.getBody());
        then(responseEntityUsuario1.getBody() instanceof IllegalArgumentException);
    }

    @Test
    public void atualizarUsuarioNaoPodeRetornarNuloEOUsuarioASerAtualizadoDeveConterID() throws IOException {
        @SuppressWarnings("rawtypes")
        ObjectMapper mapper = new ObjectMapper();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto1 = mapper.readValue(responseEntityUsuario.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto1);

        usuarioDto1.setNome(randomAlphabetic(25));
        usuarioDto1.setSobreNome(randomAlphabetic(25));
        usuarioDto1.setEmail(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3));
        usuarioDto1.setSenha(randomAlphanumeric(8));
        usuarioDto1.setSexoEnum(SexoEnum.FEMININO);
        usuarioDto1.setDataNascimento(new DateTime());

        ResponseEntity responseEntityUsuario1 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + ATUALIZAR_USUARIO, HttpMethod.PUT,
                        new HttpEntity<>(usuarioDto1, httpHeaders), String.class);

        then(responseEntityUsuario1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(responseEntityUsuario1.getBody());
        then(responseEntityUsuario1.getBody() instanceof UsuarioDto);

        ResponseEntity responseEntityUsuario2 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + BUSCAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.GET,
                        new HttpEntity<>(httpHeaders), String.class);

        then(responseEntityUsuario2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(responseEntityUsuario2.getBody());
        then(responseEntityUsuario2.getBody() instanceof UsuarioDto);


        UsuarioDto usuarioDto3 = mapper.readValue(responseEntityUsuario2.getBody().toString(), UsuarioDto.class);

        assertEquals(usuarioDto3, usuarioDto1);

        usuarioDto1.setId(null);
        ResponseEntity responseEntityUsuario3 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + ATUALIZAR_USUARIO, HttpMethod.PUT,
                        new HttpEntity<>(usuarioDto1, httpHeaders), String.class);

        then(responseEntityUsuario3.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertNotNull(responseEntityUsuario3.getBody());
        then(responseEntityUsuario3.getBody() instanceof IllegalArgumentException);
    }

    @Test
    public void naoDeixarSalvarUsuarioSemNome() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof MethodArgumentNotValidException);
    }

    @Test
    public void naoDeixarSalvarUsuarioComNomeVazio() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome("    ")
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof MethodArgumentNotValidException);
    }

    @Test
    public void naoDeixarSalvarUsuarioSemSobrenome() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof MethodArgumentNotValidException);
    }

    @Test
    public void naoDeixarSalvarUsuarioComSobrenomeVazio() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome("    ")
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof MethodArgumentNotValidException);
    }

    @Test
    public void naoDeixarSalvarUsuarioSemEmail() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof MethodArgumentNotValidException);
    }

    @Test
    public void naoDeixarSalvarUsuarioComEmailNoFormatoErrado() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof MethodArgumentNotValidException);
    }

    @Test
    public void naoDeixarSalvarUsuarioSemSexo() {
        @SuppressWarnings("rawtypes")
        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertNotNull(responseEntityUsuario.getBody());
        then(responseEntityUsuario.getBody() instanceof MethodArgumentNotValidException);
    }

    @Test
    public void buscarUsuarioPorIdNaoPodeRetornarNulo() throws IOException {
        @SuppressWarnings("rawtypes")
        HttpEntity request = new HttpEntity<>(httpHeaders);
        ObjectMapper mapper = new ObjectMapper();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto1 = mapper.readValue(responseEntityUsuario.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto1);

        ResponseEntity responseEntityUsuario2 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + BUSCAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.GET,
                        request, String.class);

        then(responseEntityUsuario2.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto2 = mapper.readValue(responseEntityUsuario2.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto2);
        assertEquals(usuarioDto2, usuarioDto1);

        ResponseEntity responseEntityUsuario3 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + BUSCAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.DELETE,
                        request, String.class);

        then(responseEntityUsuario3.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity responseEntityUsuario4 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + BUSCAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.GET,
                        request, String.class);

        then(responseEntityUsuario4.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        then(responseEntityUsuario4.getBody() instanceof NoSuchElementException);
    }

    @Test
    public void buscarTodosOsUsuarios() throws IOException {
        @SuppressWarnings("rawtypes")
        ObjectMapper mapper = new ObjectMapper();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto1 = mapper.readValue(responseEntityUsuario.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto1);

        ResponseEntity<UsuarioDto[]> responseEntityUsuarios = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + BUSCAR_TODOS_OS_USUARIOS, HttpMethod.GET,
                        new HttpEntity<>(httpHeaders), UsuarioDto[].class);

        then(responseEntityUsuarios.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<UsuarioDto> listusuarioDto = Arrays.asList(responseEntityUsuarios.getBody());

        assertNotNull(listusuarioDto);
        then(listusuarioDto.isEmpty());
        assertNotNull(listusuarioDto.get(0));
        then(listusuarioDto.get(0) instanceof UsuarioDto);
    }

    @Test
    public void deletarUsuarioPorId() throws IOException {
        @SuppressWarnings("rawtypes")
        HttpEntity request = new HttpEntity<>(httpHeaders);
        ObjectMapper mapper = new ObjectMapper();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto1 = mapper.readValue(responseEntityUsuario.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto1);

        ResponseEntity responseEntityUsuario2 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + DELETAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.DELETE,
                        request, String.class);

        then(responseEntityUsuario2.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity responseEntityUsuario3 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + BUSCAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.GET,
                        request, String.class);

        then(responseEntityUsuario3.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        then(responseEntityUsuario3.getBody() instanceof NoSuchElementException);
    }

    @Test
    public void deletarUsuarioPorIdInexistente() throws IOException {
        @SuppressWarnings("rawtypes")
        HttpEntity request = new HttpEntity<>(httpHeaders);
        ObjectMapper mapper = new ObjectMapper();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto1 = mapper.readValue(responseEntityUsuario.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto1);

        ResponseEntity responseEntityUsuario2 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + DELETAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.DELETE,
                        request, String.class);

        then(responseEntityUsuario2.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity responseEntityUsuario3 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + DELETAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.DELETE,
                        request, String.class);

        then(responseEntityUsuario3.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        then(responseEntityUsuario3.getBody() instanceof EmptyResultDataAccessException);
    }

    @Test
    public void fazerLogin() throws IOException {
        @SuppressWarnings("rawtypes")
        HttpEntity request = new HttpEntity<>(httpHeaders);
        ObjectMapper mapper = new ObjectMapper();

        UsuarioDto usuarioDto = UsuarioDto.builder()
                .nome(randomAlphabetic(25))
                .sobreNome(randomAlphabetic(25))
                .email(randomAlphabetic(7) + "@" + randomAlphabetic(5) + "."+ randomAlphabetic(3))
                .senha(randomAlphanumeric(8))
                .sexoEnum(SexoEnum.MASCULINO)
                .dataNascimento(new DateTime())
                .build();

        ResponseEntity responseEntityUsuario = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + SALVAR_USUARIO, HttpMethod.POST,
                        new HttpEntity<>(usuarioDto, httpHeaders), String.class);

        then(responseEntityUsuario.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto1 = mapper.readValue(responseEntityUsuario.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto1);

        ResponseEntity responseEntityUsuario2 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + FAZER_LOGIN + usuarioDto1.getEmail() + "/" + usuarioDto1.getSenha(),
                        HttpMethod.GET, request, String.class);

        then(responseEntityUsuario2.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsuarioDto usuarioDto2 = mapper.readValue(responseEntityUsuario2.getBody().toString(), UsuarioDto.class);

        assertNotNull(usuarioDto2);
        assertEquals(usuarioDto2, usuarioDto1);

        ResponseEntity responseEntityUsuario3 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + DELETAR_USUARIO_POR_ID + usuarioDto1.getId(), HttpMethod.DELETE,
                        request, String.class);

        then(responseEntityUsuario3.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity responseEntityUsuario4 = testRestTemplate
                .exchange(UrlPrefixFactory.getUrlPrefix() + FAZER_LOGIN + usuarioDto1.getEmail() + "/" + usuarioDto1.getSenha(),
                        HttpMethod.GET, request, String.class);

        then(responseEntityUsuario4.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        then(responseEntityUsuario4.getBody() instanceof EmptyResultDataAccessException);
    }

}
