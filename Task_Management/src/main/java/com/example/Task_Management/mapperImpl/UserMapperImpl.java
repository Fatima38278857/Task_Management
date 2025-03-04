package com.example.Task_Management.mapperImpl;


import com.example.Task_Management.dto.UserDTO;
import com.example.Task_Management.entity.UserEntity;
import org.springframework.stereotype.Component;
/*

 */
@Component
public class UserMapperImpl {
    public UserDTO toDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setRole(userEntity.getRole());
        return userDTO;
    }
    // Метод для преобразования UserDTO в UserEntity
    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(dto.getId());
        userEntity.setEmail(dto.getEmail());
        userEntity.setUsername(dto.getUsername());
        userEntity.setRole(dto.getRole());
        return userEntity;
    }
}
