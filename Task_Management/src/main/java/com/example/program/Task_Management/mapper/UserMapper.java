package com.example.program.Task_Management.mapper;

import com.example.program.Task_Management.dto.UserDTO;
import com.example.program.Task_Management.entity.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity user);
    UserEntity toEntity(UserDTO userDTO);
}
