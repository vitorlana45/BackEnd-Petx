package org.ong.pet.pex.backendpetx.service.mediaService;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MediaStorageService {
    private final MinioClient minio;
    @Value("${minio.bucket}") String bucket;
    @Value("${minio.presignSeconds}") int presignSeconds;

    public String putObject(String objectKey, String contentType, InputStream in, long size) {
        try {
            boolean exists = minio.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) minio.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());

            minio.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(in, size, -1)
                    .contentType(contentType)
                    .build()
            );
            return objectKey;
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao enviar objeto para MinIO", e);
        }
    }

    public String presignGetUrl(String objectKey) {
        try {
            return minio.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(objectKey)
                    .expiry(presignSeconds)
                    .build()
            );
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao gerar URL temporária", e);
        }
    }
}
