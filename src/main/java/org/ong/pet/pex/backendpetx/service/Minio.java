package org.ong.pet.pex.backendpetx.service;

import java.io.InputStream;

public interface Minio {

    boolean bucketExists(String bucketName);
    void createBucket(String bucketName);
    void deleteBucket(String bucketName);
    void uploadFile(String bucketName, String objectName, String filePath);

    // Novo: upload via stream (preferido para MultipartFile)
    void upload(String bucketName, String objectName, InputStream stream, long size, String contentType);

    void downloadFile(String bucketName, String objectName, String destinationPath);
    void deleteFile(String bucketName, String objectName);
    String getFileUrl(String bucketName, String objectName);
    String getFileByEntityId(String bucketName, String entityId, String fileName);

}
