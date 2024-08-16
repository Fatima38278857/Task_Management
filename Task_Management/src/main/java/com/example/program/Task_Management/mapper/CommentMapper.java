package com.example.program.Task_Management.mapper;

import com.example.program.Task_Management.dto.CommentDTO;
import com.example.program.Task_Management.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);


    CommentDTO toDTO(CommentEntity comment);

    CommentEntity toEntity(CommentDTO commentDTO);
}
