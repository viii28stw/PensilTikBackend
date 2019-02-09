package com.viii28stw.pensiltikbackend.controller;

import com.viii28stw.pensiltikbackend.model.dto.UsuarioDto;
import com.viii28stw.pensiltikbackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pensiltik")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/buscarusuarioporid/{id}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@PathVariable("id") String id){
        UsuarioDto usuarioDto = usuarioService.buscarUsuarioPorId(id);
        return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
    }

    @GetMapping("/buscartodososusuarios")
    public ResponseEntity<List<UsuarioDto>> buscarTodosOsUsuarios() {
        List<UsuarioDto> usuariosDto = usuarioService.buscarTodosOsUsuarios();
        return new ResponseEntity<>(usuariosDto, HttpStatus.OK);
    }

    @PostMapping("/salvarusuario")
    public ResponseEntity<UsuarioDto> salvarUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        return new ResponseEntity<>(usuarioService.salvarUsuario(usuarioDto), HttpStatus.OK);
    }

    @PutMapping("/atualizarusuario")
    public ResponseEntity<UsuarioDto> atualizarUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        return new ResponseEntity<>(usuarioService.atualizarUsuario(usuarioDto), HttpStatus.OK);
    }

    @DeleteMapping("/deletarusuarioporid/{id}")
    public ResponseEntity<Boolean> deletarUsuarioPorId(@PathVariable("id") String id) {
        return new ResponseEntity<>(usuarioService.deletarUsuarioPorId(id), HttpStatus.OK);
    }

    @GetMapping("/fazerlogin/{email}/{senha}")
    public ResponseEntity<UsuarioDto> login(@PathVariable("email") String email, @PathVariable("senha") String senha) {
        return new ResponseEntity<>(usuarioService.fazerLogin(email, senha), HttpStatus.OK);
    }

}
