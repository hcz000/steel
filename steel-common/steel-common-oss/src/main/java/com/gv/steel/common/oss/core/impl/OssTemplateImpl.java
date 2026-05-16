package com.gv.steel.common.oss.core.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.gv.steel.common.oss.core.OssTemplate;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@AllArgsConstructor
public class OssTemplateImpl implements OssTemplate {
    private final AmazonS3 amazonS3;

    @Override
    public void createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }
    }

    @Override
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    @Override
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception {
        return putObject(bucketName, objectName, stream, stream.available(), contextType);
    }

    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        return putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    @Override
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    @Override
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        amazonS3.deleteObject(bucketName, objectName);
    }

    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return objectListing.getObjectSummaries();
    }

    @SneakyThrows
    private PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(stream));) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(size);
            objectMetadata.setContentType(contextType);

            // 上传
            return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
        }
    }
}
