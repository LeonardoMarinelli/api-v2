package com.leo.parkapi.web.controller;

import java.util.List;
import com.leo.parkapi.entity.Usuario;
import com.leo.parkapi.service.UsuarioService;
import com.leo.parkapi.web.dto.UsuarioCreateDto;
import com.leo.parkapi.web.dto.UsuarioResponseDto;
import com.leo.parkapi.web.dto.UsuarioSenhaDto;
import com.leo.parkapi.web.dto.mapper.UsuarioMapper;
import com.leo.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários", description = "Contém as operações possíveis relativas aos usuários.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo usuário.", description = "Recurso que cria um novo usuário com base nos dados fornecidos.",
                responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "E-mail de usuário já cadastrado no sistema.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
                })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create (@Valid @RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Buscar usuário pelo ID.", description = "Recurso que busca um usuário pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado com base no ID fornecido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById (@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Alterar a senha de um usuário.", description = "Recurso que atualiza a senha de um usuário pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado com base no ID fornecido.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Senha fornecida não confere.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword (@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retornar os usuários cadastrados.", description = "Recurso que retorna uma lista com todos os usuários cadastrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))))
            })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll () {
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }
}
