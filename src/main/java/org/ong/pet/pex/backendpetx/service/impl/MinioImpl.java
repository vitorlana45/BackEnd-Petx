package org.ong.pet.pex.backendpetx.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import io.minio.errors.*;
import org.ong.pet.pex.backendpetx.service.Minio;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class MinioImpl implements Minio {

    private final MinioClient minio;

    public MinioImpl(MinioClient minio) {
        this.minio = minio;
    }

    @Override
    public boolean bucketExists(String bucketName) {
        BucketExistsArgs args = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();

        try {
            return minio.bucketExists(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 IOException | NoSuchAlgorithmException | ServerException | InvalidResponseException |
                 XmlParserException e) {
                e.printStackTrace();
                return false;
        }

    }

    @Override
    public void createBucket(String bucketName) {

        var createBucketArgs = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();

        try {
            if (!bucketExists(bucketName)) {
                minio.makeBucket(createBucketArgs);
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 IOException | NoSuchAlgorithmException | ServerException | InvalidResponseException |
                 XmlParserException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void deleteBucket(String bucketName) {
        // opcional, não utilizado no fluxo atual
    }

    @Override
    public void uploadFile(String bucketName, String objectName, String filePath) {
        try {
            if (!bucketExists(bucketName)) createBucket(bucketName);
            minio.uploadObject(
                UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(filePath)
                    .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 IOException | NoSuchAlgorithmException | ServerException | InvalidResponseException |
                 XmlParserException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void upload(String bucketName, String objectName, InputStream stream, long size, String contentType) {
        try {
            if (!bucketExists(bucketName)) createBucket(bucketName);
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(stream, size, -1)
                    .contentType(contentType != null ? contentType : "application/octet-stream")
                    .build();
            minio.putObject(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 IOException | NoSuchAlgorithmException | ServerException | InvalidResponseException |
                 XmlParserException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha no upload para o MinIO: " + e.getMessage(), e);
        }
    }

    @Override
    public void downloadFile(String bucketName, String objectName, String destinationPath) {
        // opcional, não utilizado no fluxo atual
    }

    @Override
    public void deleteFile(String bucketName, String objectName) {
        // opcional, não utilizado no fluxo atual
    }

    @Override
    public String getFileUrl(String bucketName, String objectName) {
        try {
            // URL assinada por 7 dias
            return minio.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getFileByEntityId(String bucketName, String entityId, String fileName) {
        String objectName = "animals/" + entityId + "/" + fileName;
        return getFileUrl(bucketName, objectName);
    }
}
