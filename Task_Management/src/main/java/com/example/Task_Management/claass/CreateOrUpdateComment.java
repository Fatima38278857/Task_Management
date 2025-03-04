package com.example.Task_Management.claass;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
public class CreateOrUpdateComment {
    @Schema(required = true, description = "текст комментария")
    private String text;
}
