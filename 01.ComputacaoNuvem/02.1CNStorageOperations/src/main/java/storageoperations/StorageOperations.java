package storageoperations;

import com.google.cloud.BatchResult;
import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static storageoperations.TestStorageOperations.getNameFor;

public class StorageOperations {

    Storage storage = null;

    public StorageOperations(Storage storage) {
        this.storage = storage;
    }

    public Bucket CreateBucket(String nameBucket, StorageClass storageClass, String location) {
        Bucket bucket = storage.create(
                BucketInfo.newBuilder(nameBucket)
                        // See here for possible values: http://g.co/cloud/storage/docs/storage-classes
                        .setStorageClass(storageClass)
                        // Possible values: http://g.co/cloud/storage/docs/bucket-locations#location-mr
                        .setLocation(location)
                        .build());
        return bucket;
    }

    public void deleteBucket() {
        String bucketName = getNameFor("Bucket name");
        Bucket bucket = storage.get(bucketName);
        bucket.delete();
    }



    public void listBuckets(String projID) {
        System.out.println("Buckets in Project = " + projID + ":");

        for (Bucket bucket : storage.list().iterateAll()) {
            System.out.println("  " + bucket.toString());
            for (Blob blob : bucket.list().iterateAll()) {
                System.out.println("      " + blob.toString());
            }

        }
    }

    public void deleteAllBlobsOfBucket() {
        String bucketName = getNameFor("Bucket name");
        Bucket bucket = storage.get(bucketName);
        for (Blob blob : bucket.list().iterateAll()) {
            System.out.println("      " + blob.toString());
            blob.delete();
        }
    }


    public void batchDeleteAllBlobs() {
        String bucketName = getNameFor("Bucket name");
        Bucket bucket = storage.get(bucketName);

        StorageBatch batch=storage.batch();
        for (Blob blob : bucket.list().iterateAll()) {
            System.out.println("      " + blob.toString());
            final String blobName=blob.getName();
            batch.delete(blob.getBlobId()).notify(new BatchResult.Callback<Boolean, StorageException>() {
                @Override
                public void success(Boolean aBoolean) {
                    System.out.println(blobName+" was deleted");
                }
                @Override
                public void error(StorageException e) {
                    e.printStackTrace();
                }
            });
        }
        batch.submit();
        System.out.println("Batch Submitted");
    }

    public BlobId addBlobToBucket() throws Exception {
        String bucketName = getNameFor("Bucket name");
        String blobName = getNameFor("Blob name");
        String absFileName = getNameFor("Pathname of file");
        //ChooseFileToUpload();
        Path uploadFrom = Paths.get(absFileName);
        String contentType = Files.probeContentType(uploadFrom);
        //System.out.println(contentType);
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        if (Files.size(uploadFrom) > 1_000_000) {
            // When content is not available or large (1MB or more) it is recommended
            // to write it in chunks via the blob's channel writer.
            try (WriteChannel writer = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                try (InputStream input = Files.newInputStream(uploadFrom)) {
                    int limit;
                    while ((limit = input.read(buffer)) >= 0) {
                        try {
                            writer.write(ByteBuffer.wrap(buffer, 0, limit));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } else {
            byte[] bytes = Files.readAllBytes(uploadFrom);
            // create the blob in one request.
            storage.create(blobInfo, bytes);
        }
        System.out.println("Blob access URL: " + "https://storage.googleapis.com/" + bucketName + "/" + blobName);
        return blobId;
    }

    public void changeBlobAccessPublicReader() {
        String bucketName = getNameFor("Bucket name");
        String blobName = getNameFor("name of Blob to change permissions");
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        Acl.Entity aclEnt = Acl.User.ofAllUsers();
        //Acl.Role[] roles = Acl.Role.values();
        Acl.Role role = Acl.Role.READER;
        Acl acl = Acl.newBuilder(aclEnt, role).build();
        blob.createAcl(acl);
        //Acess blob via URL:
        //https://storage.googleapis.com/cn-v1819-demo/The-Google-File-System-sosp2003.pdf
        System.out.println("Blob access URL: " + "https://storage.googleapis.com/" + bucketName + "/" + blobName);
    }

    // RETIRAR?
    public void changeBlobToWriteAccess() {
        String bucketName = getNameFor("Bucket name");
        String blobName = getNameFor("name of Blob to change permissions");
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        Acl.Entity aclEnt = Acl.User.ofAllAuthenticatedUsers();
        // Not available for Blobs (see https://cloud.google.com/storage/docs/access-control/lists#permissions)
        //Acl.Role role = Acl.Role.WRITER;
        Acl.Role role = Acl.Role.OWNER;
        Acl acl = Acl.newBuilder(aclEnt, role).build();
        blob.createAcl(acl);
        System.out.println("Write permission granted");
    }

    public void changeBlobMetadata() {
        String bucketName = getNameFor("Bucket name");
        String blobName = getNameFor("name of Blob to change metadata");
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        Map<String, String> metadata = new HashMap<>();
        Scanner scan = new Scanner(System.in);
        System.out.println("Introduce key, value pairs for each line, ended by \"end\" ");
        for (; ; ) {
            String line = scan.nextLine();
            String[] par = line.split(",");
            if (par[0].equals("end")) break;
            metadata.put(par[0], par[1]);
        }
        blob.toBuilder().setCacheControl("private").build().update();
        Blob updateBlob = blob.toBuilder().setMetadata(metadata).build().update();
    }

    public void downloadBlob() throws IOException {
        String bucketName = getNameFor("Bucket name");
        String blobName = getNameFor("Blob name");
        String absFileName = getNameFor("file Pathname for downloading the Blob");
        //ChooseFileToUpload();
        Path downloadTo = Paths.get(absFileName);
        System.out.println("download to: "+downloadTo.toString());
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        //blob.downloadTo(Paths.get("D:\\BLOB"));
        if (blob == null) {
            System.out.println("No such Blob exists !");
            return;
        }
        //File f=downloadTo.toFile();
        //System.out.println(f.getAbsolutePath()+" : canonical->"+f.getCanonicalPath());
        //PrintStream writeTo = new PrintStream(new FileOutputStream(downloadTo.toFile()));
        //PrintStream writeTo = new PrintStream(new FileOutputStream(absFileName));
        PrintStream writeTo = new PrintStream(Files.newOutputStream(downloadTo));
        if (blob.getSize() < 1_000_000) {
            // Blob is small read all its content in one request
            byte[] content = blob.getContent();
            writeTo.write(content);
        } else {
            // When Blob size is big or unknown use the blob's channel reader.
            try (ReadChannel reader = blob.reader()) {
                WritableByteChannel channel = Channels.newChannel(writeTo);
                ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
                while (reader.read(bytes) > 0) {
                    bytes.flip();
                    channel.write(bytes);
                    bytes.clear();
                }
            }
        }
        writeTo.close();
    }

    public void deleteBlob() {
        String bucketName = getNameFor("Bucket name");
        String blobName = getNameFor("name of Blob to delete");
        BlobId blobId = BlobId.of(bucketName, blobName);
        if (storage.delete(blobId))
            System.out.println("Blob deleted");
        else
            System.out.println("Blob not deleted!!!");
    }


    /*
    private String ChooseFileToUpload() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setVisible(true);
        //JDialog dialog = new JDialog();
        String absFileName = null;
        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
            absFileName = selectedFile.getAbsolutePath();
        }
        return absFileName;
    }
    */

}
