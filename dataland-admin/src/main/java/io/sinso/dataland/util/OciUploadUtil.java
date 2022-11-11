package io.sinso.dataland.util;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadResponse;

import java.io.File;
import java.io.InputStream;

/**
 * @author hengbol
 * @date 7/22/22 5:19 PM
 */
public class OciUploadUtil {


    public static String uploadOci(File body, String name, String contentType) {
        try {
            String namespaceName = "cnjdgjb7k2rm";
            String bucketName = "sinso-dataland";
            final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
            final ConfigFileAuthenticationDetailsProvider provider =
                    new ConfigFileAuthenticationDetailsProvider(configFile);

            ObjectStorage client = new ObjectStorageClient(provider);
            client.setRegion(Region.AP_SEOUL_1);

            // configure upload settings as desired
            UploadConfiguration uploadConfiguration =
                    UploadConfiguration.builder()
                            .allowMultipartUploads(true)
                            .allowParallelUploads(true)
                            .build();

            UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucketName(bucketName)
                    .namespaceName(namespaceName)
                    .objectName(name)
                    .contentType(contentType)
                    .build();
            UploadRequest uploadDetails = UploadRequest.builder(body).allowOverwrite(true).build(request);

            // upload request and print result
            // if multi-part is used, and any part fails, the entire upload fails and will throw BmcException
            UploadResponse response = uploadManager.upload(uploadDetails);
            // fetch the object just uploaded
            GetObjectResponse getResponse =
                    client.getObject(
                            GetObjectRequest.builder()
                                    .namespaceName(namespaceName)
                                    .bucketName(bucketName)
                                    .objectName(name)
                                    .build());
            //fingerprint=aa:2f:c5:cf:54:67:c9:3c:c5:94:3b:73:8a:6e:36:b2
            //d6:e2:04:5b:29:b0:2e:a0:3c:0f:29:94:25:de:b7:3d
            // use the response's function to print the fetched object's metadata
            String url = "https://objectstorage.ap-seoul-1.oraclecloud.com/n/cnjdgjb7k2rm/b/sinso-dataland/o/" + name;
            // stream contents should match the file uploaded
            try (final InputStream fileStream = getResponse.getInputStream()) {
                // use fileStream
            } // try-with-resources automatically closes fileStream
            FileUtils.delTempFile(body);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        uploadOci(new File("/Users/hengbol/JAVA/IdeaProjects/NFT/dataland/dataland-admin/src/main/resources/test.glb"), "2.json", null);
    }
}
