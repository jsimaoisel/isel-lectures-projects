package service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import servicecontract.Contents;
import servicecontract.FileUploadServiceGrpc;
import servicecontract.Result;
import servicecontract.TestString;

public class Server extends FileUploadServiceGrpc.FileUploadServiceImplBase {

    private static int svcPort = 8000;
    static Storage storage = null;
    static GoogleCredentials credentials = null;
    static StorageOptions storageOptions=null;
    static Firestore dbFirestore=null;
    static String bucketName="cn2122-testmixservices";
    static String currentCollection="cn2122-testmixservices";
    static String PROJECT_ID="cn2122-jsla-geral";

    public static void main(String[] args) {

        try {
            PubSub.listTopics(PROJECT_ID);
            if (args.length > 0) {
                svcPort = Integer.parseInt(args[0]);
            }
            storageOptions= StorageOptions.getDefaultInstance();
            storage = storageOptions.getService();
            credentials = GoogleCredentials.getApplicationDefault();
            FirestoreOptions options = FirestoreOptions
                    .newBuilder().setCredentials(credentials).build();
            dbFirestore = options.getService();

            io.grpc.Server svc = ServerBuilder
                    .forPort(svcPort)
                    .addService(new Server())
                    .build();
            svc.start();
            System.out.println("server start as port " + svcPort);
            // do another things
            svc.awaitTermination();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void test(TestString request, StreamObserver<TestString> responseObserver) {
     //DetectTranslateAPIs.testVisionLabels(request.getStr());
     responseObserver.onNext(TestString.newBuilder().setStr("OK").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Contents> sendFileBlocks(StreamObserver<Result> responseObserver) {
        return new StreamFileBlocks(storage, dbFirestore,bucketName,currentCollection, PROJECT_ID,responseObserver);
    }
}
