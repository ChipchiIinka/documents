package ru.egartech.documents.utils;

import ru.egartech.documents.exceptions.DocumentsException;
import ru.egartech.documents.exceptions.ErrorType;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeUtil {
    private static final Map<String, String> mimeTypeMap = new HashMap<>();

    static {
        mimeTypeMap.put("application/pdf", "PDF Document");
        mimeTypeMap.put("application/msword", "Microsoft Word Document");
        mimeTypeMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Microsoft Word Document 2007");
        mimeTypeMap.put("image/jpeg", "JPEG Image");
        mimeTypeMap.put("image/png", "PNG Image");
        mimeTypeMap.put("text/plain", "Text");
        mimeTypeMap.put("text/html", "HTML Document");
        mimeTypeMap.put("application/json", "JavaScript Object Notation (JSON)");
        mimeTypeMap.put("application/javascript", "JavaScript");
        mimeTypeMap.put("application/postscript", "PostScript");
        mimeTypeMap.put("application/soap+xml", "SOAP");
        mimeTypeMap.put("application/xhtml+xml", "XHTML");
        mimeTypeMap.put("application/xml", "Extensible Markup Language (XML)");
        mimeTypeMap.put("application/x-yaml", "YAML");
        mimeTypeMap.put("audio/basic", "Basic Audio");
        mimeTypeMap.put("audio/mp4", "MP4 Audio");
        mimeTypeMap.put("audio/mpeg", "MPEG Audio");
        mimeTypeMap.put("audio/ogg", "Ogg Audio");
        mimeTypeMap.put("audio/vnd.rn-realaudio", "RealAudio");
        mimeTypeMap.put("audio/vnd.wave", "WAV Audio");
        mimeTypeMap.put("image/gif", "GIF Image");
        mimeTypeMap.put("image/svg+xml", "Scalable Vector Graphics (SVG)");
        mimeTypeMap.put("image/tiff", "Tagged Image File Format (TIFF)");
        mimeTypeMap.put("multipart/form-data", "Form Data");
        mimeTypeMap.put("text/css", "Cascading Style Sheets (CSS)");
        mimeTypeMap.put("text/xml", "XML");
        mimeTypeMap.put("video/mp4", "MP4 Video");
        mimeTypeMap.put("video/ogg", "Ogg Video");
        mimeTypeMap.put("video/webm", "WebM Video");
        mimeTypeMap.put("application/epub+zip", "EPUB Document");
        mimeTypeMap.put("application/gzip", "GZip Compressed Archive");
        mimeTypeMap.put("application/java-archive", "Java Archive (JAR)");
        mimeTypeMap.put("application/vnd.oasis.opendocument.text", "OpenDocument Text Document");
        mimeTypeMap.put("application/vnd.oasis.opendocument.spreadsheet", "OpenDocument Spreadsheet");
        mimeTypeMap.put("application/vnd.oasis.opendocument.presentation", "OpenDocument Presentation");
        mimeTypeMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "Microsoft PowerPoint Presentation 2007");
        mimeTypeMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Microsoft Excel Spreadsheet 2007");
        mimeTypeMap.put("application/zip", "ZIP Archive");
        mimeTypeMap.put("audio/aac", "AAC Audio");
        mimeTypeMap.put("audio/flac", "FLAC Audio");
        mimeTypeMap.put("audio/x-aiff", "AIFF Audio");
        mimeTypeMap.put("image/bmp", "Bitmap Image (BMP)");
        mimeTypeMap.put("image/webp", "WebP Image");
        mimeTypeMap.put("text/csv", "Comma-Separated Values (CSV)");
        mimeTypeMap.put("application/vnd.ms-excel", "Microsoft Excel Spreadsheet");
        mimeTypeMap.put("application/vnd.ms-powerpoint", "Microsoft PowerPoint Presentation");
        mimeTypeMap.put("application/vnd.visio", "Microsoft Visio Document");
        mimeTypeMap.put("application/x-7z-compressed", "7-zip Archive");
        mimeTypeMap.put("application/x-rar-compressed", "RAR Archive");
        mimeTypeMap.put("application/x-tar", "TAR Archive");
        mimeTypeMap.put("application/x-bzip", "BZip Archive");
        mimeTypeMap.put("application/x-bzip2", "BZip2 Archive");
        mimeTypeMap.put("application/x-csh", "C Shell Script");
        mimeTypeMap.put("application/x-sh", "Bourne Shell Script");
        mimeTypeMap.put("application/x-shockwave-flash", "Shockwave Flash");
        mimeTypeMap.put("application/x-www-form-urlencoded", "URL Encoded Form Data");
        mimeTypeMap.put("application/x-httpd-php", "PHP Script");
        mimeTypeMap.put("application/x-pkcs12", "PKCS#12 Archive");
        mimeTypeMap.put("application/x-pkcs7-certificates", "PKCS#7 Certificates");
        mimeTypeMap.put("application/x-pkcs7-certreqresp", "PKCS#7 Certificate Request Response");
        mimeTypeMap.put("application/x-x509-ca-cert", "X.509 CA Certificate");
        mimeTypeMap.put("application/octet-stream", "Binary Data");
        mimeTypeMap.put("font/woff", "Web Open Font Format (WOFF)");
        mimeTypeMap.put("font/woff2", "Web Open Font Format (WOFF2)");
        mimeTypeMap.put("font/otf", "OpenType Font");
        mimeTypeMap.put("font/ttf", "TrueType Font");
        mimeTypeMap.put("application/vnd.apple.installer+xml", "Apple Installer Package");
        mimeTypeMap.put("application/vnd.mozilla.xul+xml", "XUL");
        mimeTypeMap.put("application/vnd.ms-fontobject", "Microsoft Embedded OpenType (EOT) Font");
        mimeTypeMap.put("application/x-abiword", "AbiWord Document");
        mimeTypeMap.put("application/x-freearc", "FreeArc Archive");
        mimeTypeMap.put("application/x-iso9660-image", "ISO Disk Image");
        mimeTypeMap.put("application/x-zip-compressed", "ZIP Archive");
    }

    private MimeTypeUtil() {
        throw new DocumentsException(ErrorType.COMMON_ERROR);
    }

    public static String getReadableMimeType(String mimeType) {
        return mimeTypeMap.getOrDefault(mimeType, mimeType);
    }
}
