package ru.egartech.documents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.egartech.documents.dto.FileResponseDto;
import ru.egartech.documents.service.FileService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FileViewController {
    private final FileService fileService;

    @GetMapping
    public String getAllFiles(@RequestParam(required = false, defaultValue = "name") String sortField,
                              @RequestParam(required = false, defaultValue = "asc") String sortOrder,
                              Model model) {
        List<FileResponseDto> files = fileService.findAllSorted(sortField, sortOrder);
        model.addAttribute("files", files);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        return "index";
    }

    @GetMapping("/add")
    public String addFileForm() {
        return "add";
    }

    @GetMapping("/change/{id}")
    public String changeFileForm(@PathVariable UUID id, Model model) {
        FileResponseDto file = fileService.findById(id);
        model.addAttribute("file", file);
        return "change";
    }

    @GetMapping("/update/{id}")
    public String updateFileForm(@PathVariable UUID id, Model model) {
        FileResponseDto file = fileService.findById(id);
        model.addAttribute("file", file);
        return "update";
    }
}
