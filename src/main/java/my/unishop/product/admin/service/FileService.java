package my.unishop.product.admin.service;

import java.io.IOException;

public interface FileService {
    String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws IOException;
}
