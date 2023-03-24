package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.WriteChannel;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import servicecontract.Contents;
import servicecontract.Result;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StreamFileBlocks implements StreamObserver<Contents> {

    Storage storage=null;
    Firestore db=null;
    String projID=null;
    StreamObserver<Result> responseObserver=null;
    String bucketName="cn2122-testmixservices";
    String currentCollection="cn2122-testmixservices";
    String blobName=null;
    String ContentType=null;
    WriteChannel writer=null;
    int numBlocks=0; long size=0;

    public StreamFileBlocks(Storage storage, Firestore dbFirestore, String currentCollection, String bucketName, String projID, StreamObserver<Result> responseObserver) {
        this.storage=storage;
        this.bucketName=bucketName;
        this.db=dbFirestore;
        this.currentCollection=currentCollection;
        this.projID=projID;
        this.responseObserver=responseObserver;
    }

    @Override
    public void onNext(Contents contents) {
      System.out.println("more one block");
        BlobId blobId = BlobId.of(bucketName, contents.getFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contents.getContentType()).build();
        if (writer == null) {
            blobName=contents.getFilename();
            writer=storage.writer(blobInfo);
            ContentType=contents.getContentType();
        }
        byte[] buffer=contents.getFileBlockBytes().toByteArray();
        try {
            numBlocks++; size+=buffer.length;
            ByteBuffer bytebuffer=ByteBuffer.wrap(buffer, 0, buffer.length);
            writer.write(bytebuffer);

        } catch (IOException e) {
            e.printStackTrace();
            Throwable th=new StatusException(Status.INVALID_ARGUMENT.withDescription(e.getMessage()));
            responseObserver.onError(th);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageDigest funcDigest = null;
        try {
            funcDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String URI="gs://"+bucketName+"/"+blobName;
        byte[] arr=funcDigest.digest(URI.getBytes());
        String hashID= Base64.getEncoder().withoutPadding().encodeToString(arr);
        Result res=Result.newBuilder().setNumBlocksReceived(numBlocks).setHashId(hashID).build();
        responseObserver.onNext(res);
        responseObserver.onCompleted();
        // obter labels
        List<String> labels=new ArrayList<>();
        try {
            labels= DetectTranslateAPIs.detectLabels(blobName);
        } catch (IOException e) {
            e.printStackTrace();
        }
         // colocar no firestore
        Document doc=new Document();
        doc.blobName=blobName; doc.docID=bucketName+"-"+blobName; doc.hashID=hashID;
        doc.nBlocks=numBlocks; doc.size=size; doc.contentType=ContentType;
        doc.labels=labels;
        doc.labelsTranslated= DetectTranslateAPIs.TranslateLabels(doc.labels);
        CollectionReference colRef=db.collection(currentCollection);
        //DocumentReference docRef = colRef.document(doc.docID);
        DocumentReference docRef=colRef.document();
        ApiFuture<WriteResult> futres = docRef.set(doc);
        try {
            futres.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        PubSub.Subscribe(projID);
        PubSub.Publish(projID,doc);
    }
}
