package com.example.program.Task_Management.controller;

import com.example.program.Task_Management.claass.UserProfileRequest;
import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.Register;
import com.example.program.Task_Management.entity.UserEntity;
import com.example.program.Task_Management.mapper.UserMapper;
import com.example.program.Task_Management.mapperr.UserMapperr;
import com.example.program.Task_Management.repository.RegisterRepository;
import com.example.program.Task_Management.repository.UserRepository;
import com.example.program.Task_Management.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterRepository registerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMapperr userMapperr;

    @Operation(summary = "Создать профиль пользователя", description = "Создает новый профиль пользователя, если он не существует.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль пользователя успешно создан"),
            @ApiResponse(responseCode = "400", description = "Профиль пользователя уже существует", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @PostMapping("/createProfile")
    public ResponseEntity<String> createProfile( @RequestBody(description = "Данные пользователя для создания профиля", required = true, content = @Content(schema = @Schema(implementation = UserProfileRequest.class)))
                                                     UserProfileRequest request,
                                                 Principal principal) {
        // Находим регистрацию по имени пользователя из principal (которое извлекается из токена)
        Register register = registerRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + principal.getName()));

        // Проверяем, есть ли уже профиль пользователя
        if (userRepository.findByRegister(register).isPresent()) {
            return ResponseEntity.badRequest().body("Профиль пользователя уже существует");
        }

        // Создаем учетные данные
        UserEntity user = new UserEntity(request.getEmail(), request.getRole(), register);
        userRepository.save(user);

        return ResponseEntity.ok("Профиль пользователя успешно создан");
    }
    @Operation(summary = "Получить данные пользователя по ID", description = "Возвращает данные пользователя на основе ID пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserEntity userEntity = userService.findById(id);
        UserDTO userDTO = userMapperr.toDTO(userEntity);
        return ResponseEntity.ok(userDTO);
    }
}
