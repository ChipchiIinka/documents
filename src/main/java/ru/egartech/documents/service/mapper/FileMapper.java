package ru.egartech.documents.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;
import ru.egartech.documents.dto.FileResponseDto;
import ru.egartech.documents.entity.FileEntity;

import java.io.IOException;

@Mapper
public interface FileMapper {
    @Mapping(target = "lastModified", dateFormat = "HH:mm dd.MM.yyyy")
    FileResponseDto toFileResponseDto(FileEntity file);

    @Mapping(target = "name", expression = "java(file.getOriginalFilename())")
    @Mapping(target = "contentType", expression = "java(file.getContentType())")
    @Mapping(target = "size", expression = "java(file.getSize())")
    @Mapping(target = "data", expression = "java(file.getBytes())")
    FileEntity toFileEntity(MultipartFile file) throws IOException;
}
