package ru.egartech.documents.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.egartech.documents.dto.FileResponseDto;
import ru.egartech.documents.exceptions.baseresponse.BaseResponseService;
import ru.egartech.documents.exceptions.baseresponse.ResponseWrapper;
import ru.egartech.documents.service.FileService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {
    private final FileService fileService;
    private final BaseResponseService baseResponseService;

    @Operation(summary = "Получить список всех файлов в системе")
    @GetMapping
    public ResponseWrapper<List<FileResponseDto>> getAllFiles(@RequestParam(required = false) String sort) {
        List<FileResponseDto> files = fileService.findAll();
        return baseResponseService.wrapSuccessResponse(files);
    }

    @Operation(summary = "Получить список всех файлов, номер, имя или тип которых содержат данные поисковой строки")
    @GetMapping("/search")
    public ResponseWrapper<List<FileResponseDto>> searchFiles(
            @RequestParam(required = false) String searchRequest,
            @RequestParam(required = false, defaultValue = "name") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        List<FileResponseDto> files = fileService.searchByIdOrNameOrType(searchRequest != null ? searchRequest : "", sortField, sortOrder);
        return baseResponseService.wrapSuccessResponse(files);
    }

    @Operation(summary = "Скачать файл по id")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) {
        return fileService.downloadById(id);
    }

    @Operation(summary = "Загрузить новый файл в систему")
    @PostMapping("/upload")
    public ResponseWrapper<?> uploadFile(@RequestParam @Valid MultipartFile file,
                                         @RequestParam String description) throws IOException {
        fileService.save(file, description);
        return baseResponseService.wrapSuccessResponse(
                String.format("Файл успешно загружен: %s", file.getOriginalFilename()));
    }

    @Operation(summary = "Изменить имя существующего файла по id")
    @PutMapping("/{id}/changeName")
    public ResponseWrapper<?> changeFileName(@PathVariable UUID id,
                                             @RequestParam @Valid String name,
                                             @RequestParam String description) {
        fileService.changeName(id, name, description);
        return baseResponseService.wrapSuccessResponse(
                String.format("Имя файла успешно изменено: %s", name));
    }

    @Operation(summary = "Обновить данные файла по id")
    @PutMapping("/{id}/update")
    public ResponseWrapper<?> updateFile(@PathVariable UUID id,
                                         @RequestPart(value = "file") @Valid MultipartFile file,
                                         @RequestParam String description) throws IOException {
        fileService.update(id, file, description);
        return baseResponseService.wrapSuccessResponse(
                String.format("Файл успешно обновлен: %s", file.getOriginalFilename()));
    }

    @Operation(summary = "Удалить файл по id")
    @DeleteMapping("/{id}/delete")
    public ResponseWrapper<?> deleteFile(@PathVariable UUID id) {
        fileService.deleteById(id);
        return baseResponseService.wrapSuccessResponse("Файл успешно удален");
    }

    @Operation(summary = "Создать файл со статистикой обновляемых файлов за определенный промежуток времени")
    @PostMapping("/statistic")
    public ResponseWrapper<?> createStatisticDocument(@RequestParam LocalDate startDate,
                                                      @RequestParam LocalDate endDate) {
        fileService.createStatisticDocument(startDate, endDate);
        return baseResponseService.wrapSuccessResponse("Документ со статистикой успешно сгенерирован");
    }
}
