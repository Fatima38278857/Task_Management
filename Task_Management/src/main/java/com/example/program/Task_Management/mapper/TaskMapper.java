package com.example.program.Task_Management.mapper;

import com.example.program.Task_Management.dto.TaskDTO;
import com.example.program.Task_Management.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    TaskDTO taskToTaskDTO(TaskEntity task);


    TaskEntity taskDTOToTask(TaskDTO taskDTO);
}
