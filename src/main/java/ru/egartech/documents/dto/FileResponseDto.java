package ru.egartech.documents.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Документ")
public class FileResponseDto {
    @Schema(description = "Id файла")
    private String id;

    @Schema(description = "Имя файла")
    private String name;

    @Schema(description = "Размер документа в байтах")
    private Long size;

    @Schema(description = "Тип документа")
    private String contentType;

    @Schema(description = "Краткое Описание файла, Опционально")
    private String description;

    @Schema(description = "Последнее обновление файла")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private String lastModified;
}
