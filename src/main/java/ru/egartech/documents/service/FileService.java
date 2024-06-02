package ru.egartech.documents.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.egartech.documents.dto.FileResponseDto;
import ru.egartech.documents.entity.FileEntity;
import ru.egartech.documents.exceptions.DocumentsException;
import ru.egartech.documents.exceptions.ErrorType;
import ru.egartech.documents.repository.FileRepository;
import ru.egartech.documents.service.mapper.FileMapper;
import ru.egartech.documents.utils.MimeTypeUtil;
import ru.egartech.documents.utils.StatisticCreator;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    private static final String DOCX_TYPE = "Microsoft Word Document 2007";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<FileResponseDto> findAll() {
        log.info("Find all files");
        List<FileEntity> entities = fileRepository.findAll();
        return entities.stream().map(fileMapper::toFileResponseDto).toList();
    }

    public List<FileResponseDto> findAllSorted(String sortField, String sortOrder) {
        log.info("Find all method with sort: {} {}", sortField, sortOrder);
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        List<FileEntity> entities = fileRepository.findAll(sort);
        return entities.stream().map(fileMapper::toFileResponseDto).toList();
    }

    public FileResponseDto findById(UUID id){
        log.info("Find file by id: {}", id);
        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new DocumentsException(ErrorType.NOT_FOUND));
        return fileMapper.toFileResponseDto(file);
    }

    @Transactional
    public List<FileResponseDto> searchByIdOrNameOrType(String searchRequest, String sortField, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
        List<FileEntity> entities = fileRepository.findBySearchRequest(searchRequest, sort);
        return entities.stream().map(fileMapper::toFileResponseDto).toList();
    }

    public ResponseEntity<byte[]> downloadById(UUID id) {
        log.info("Download file by id: {}", id);
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new DocumentsException(ErrorType.NOT_FOUND));
        String encodedFilename = URLEncoder.encode(fileEntity.getName(), StandardCharsets.UTF_8)
                .replace("+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", encodedFilename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileEntity.getData());
    }

    public void save(MultipartFile multipartFile, String description) throws IOException {
        log.info("Uploading new file: {}; and description: {}", multipartFile, description);
        FileEntity fileEntity = fileMapper.toFileEntity(multipartFile);
        String mimeType = multipartFile.getContentType();
        String readableMimeType = MimeTypeUtil.getReadableMimeType(mimeType);
        fileEntity.setContentType(readableMimeType);
        fileEntity.setLastModified(LocalDateTime.now());
        fileEntity.setDescription(description);
        fileRepository.save(fileEntity);
    }

    public void changeName(UUID id, String newName, String description) {
        log.info("Rename file with id: {}, name: {}, and description: {}", id, newName, description);
        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new DocumentsException(ErrorType.NOT_FOUND));
        String originalFileExtension = getFileExtension(file.getName());
        if (isValidFileName(newName)){
            file.setName(newName + originalFileExtension);
            file.setDescription(description);
            file.setLastModified(LocalDateTime.now());
            fileRepository.save(file);
        } else {
            throw new DocumentsException(ErrorType.NOT_VALID_NAME);
        }
    }

    public void update(UUID id, MultipartFile updatedFile, String description) throws IOException {
        log.info("Updating file by id: {}, updated file: {}; and description: {}", id, updatedFile, description);
        FileEntity repoFile = fileRepository.findById(id)
                .orElseThrow(() -> new DocumentsException(ErrorType.NOT_FOUND));
        if (Objects.equals(updatedFile.getOriginalFilename(), repoFile.getName())){
            repoFile.setSize(updatedFile.getSize());
            repoFile.setData(updatedFile.getBytes());
            repoFile.setLastModified(LocalDateTime.now());
            repoFile.setDescription(description);
            fileRepository.save(repoFile);
        } else {
            throw new DocumentsException(ErrorType.MUST_BE_SAME);
        }
    }

    public void deleteById(UUID id){
        log.info("Delete file by id: {}", id);
        fileRepository.deleteById(id);
    }

    @Transactional
    public void createStatisticDocument(LocalDate periodStart, LocalDate periodEnd) {
        log.info("Creating statistic document with period: {} - {}", periodStart, periodEnd);
        LocalDateTime startDateTime = periodStart.atStartOfDay();
        LocalDateTime endDateTime = periodEnd.atTime(LocalTime.MAX);
        List<FileEntity> files = fileRepository.findAllByLastModifiedBetween(startDateTime, endDateTime);
        FileEntity statisticDocument = new FileEntity();
        String fileName = String.format("Статистика файлов за период %s - %s.docx",
                periodStart.format(formatter), periodEnd.format(formatter));
        StatisticCreator statisticCreator = new StatisticCreator();
        byte[] data = statisticCreator.createStatistic(files, periodStart, periodEnd);
        statisticDocument.setName(fileName);
        statisticDocument.setContentType(DOCX_TYPE);
        statisticDocument.setSize((long) data.length);
        statisticDocument.setData(data);
        statisticDocument.setDescription(
                String.format("Статистика добавления/редактирования файлов по типам за период %s - %s",
                        periodStart.format(formatter), periodEnd.format(formatter)));
        statisticDocument.setLastModified(LocalDateTime.now());
        fileRepository.save(statisticDocument);
    }

    private boolean isValidFileName(String fileName) {
        String regex = "^[^<>:\"/\\\\|?*;]+$";
        return fileName.matches(regex);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
}