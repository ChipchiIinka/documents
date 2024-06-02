package ru.egartech.documents.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import ru.egartech.documents.entity.FileEntity;
import ru.egartech.documents.exceptions.DocumentsException;
import ru.egartech.documents.exceptions.ErrorType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticCreator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public byte[] createStatistic(List<FileEntity> files, LocalDate periodStart, LocalDate periodEnd) {
        XWPFDocument document = new XWPFDocument();
        Map<String, Long> typesCounting = countFileTypes(files);
        long totalFiles = files.size();
        long totalSize = files.stream().mapToLong(FileEntity::getSize).sum();
        LocalDateTime lastModified = files.stream()
                .map(FileEntity::getLastModified)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        addStyles(document);
        addTitle(document, periodStart, periodEnd);
        addSummary(document, totalFiles, totalSize, lastModified);
        addTable(document, typesCounting);
        return saveDocumentToByteArray(document);
    }

    private void addStyles(XWPFDocument document) {
        XWPFStyles styles = document.createStyles();
        CTFonts fonts = CTFonts.Factory.newInstance();
        fonts.setAscii("Times New Roman");
        styles.setDefaultFonts(fonts);
    }

    private void addTitle(XWPFDocument document, LocalDate periodStart, LocalDate periodEnd) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(String.format("Статистика редактирования файлов за период (%s - %s)",
                periodStart.format(formatter), periodEnd.format(formatter)));
        titleRun.addCarriageReturn();
        titleRun.setBold(true);
        titleRun.setFontSize(14);
    }

    private void addSummary(XWPFDocument document, long totalFiles, long totalSize, LocalDateTime lastModified) {
        XWPFParagraph summary = document.createParagraph();
        summary.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun summaryRun = summary.createRun();
        summaryRun.setText(String.format("Общее количество файлов: %d", totalFiles));
        summaryRun.addCarriageReturn();
        summaryRun.setText(String.format("Общий размер файлов: %s", formatSize(totalSize)));
        summaryRun.addCarriageReturn();
        if (lastModified != null) {
            summaryRun.setText(String.format("Самый последний измененный файл: %s", lastModified.format(formatter)));
            summaryRun.addCarriageReturn();
        }
        summaryRun.setFontSize(12);
    }

    private void addTable(XWPFDocument document, Map<String, Long> typesCounting) {
        XWPFTable table = document.createTable();
        table.setWidth("100%");
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJcTable jcTable = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
        jcTable.setVal(STJcTable.CENTER);
        XWPFTableRow headerRow = table.getRow(0);
        setHeaderCellStyle(headerRow.getCell(0)).setText("Тип файла");
        setHeaderCellStyle(headerRow.addNewTableCell()).setText("Количество файлов");
        typesCounting.forEach((type, count) -> {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(type);
            row.getCell(1).setText(String.valueOf(count));
            setCellStyle(row);
        });
    }

    private XWPFTableCell setHeaderCellStyle(XWPFTableCell cell) {
        CTTcPr tcPr = cell.getCTTc().addNewTcPr();
        CTShd shd = tcPr.addNewShd();
        shd.setFill("CCCCCC");
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        cell.getParagraphs().forEach(paragraph -> {
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            paragraph.setIndentationLeft(100);
            paragraph.setIndentationRight(100);
            XWPFRun run = paragraph.createRun();
            run.setBold(true);
        });
        return cell;
    }

    private void setCellStyle(XWPFTableRow row) {
        for (XWPFTableCell cell : row.getTableCells()) {
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            cell.getParagraphs().forEach(paragraph -> {
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                paragraph.setIndentationLeft(100);
                paragraph.setIndentationRight(100);
            });
        }
    }

    private byte[] saveDocumentToByteArray(XWPFDocument document) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            document.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new DocumentsException(ErrorType.COMMON_ERROR);
        }
    }

    private Map<String, Long> countFileTypes(List<FileEntity> files) {
        return files.stream()
                .collect(Collectors.groupingBy(FileEntity::getContentType, Collectors.counting()));
    }

    private String formatSize(long size) {
        if (size < 1024) {
            return size + " байт";
        }
        int exp = (int) (Math.log(size) / Math.log(1024));
        char unit = "КМГТП".charAt(exp - 1);
        return String.format("%.1f %sБ", size / Math.pow(1024, exp), unit);
    }
}