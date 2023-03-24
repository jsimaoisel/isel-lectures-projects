package client;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import servicecontract.Contents;
import servicecontract.FileUploadServiceGrpc;
import servicecontract.Result;
import servicecontract.TestString;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ClientApp {

    private static String svcIP = "localhost";

    private static int svcPort = 8000;
    private static ManagedChannel channel;
    private static FileUploadServiceGrpc.FileUploadServiceBlockingStub blockingStub;
    private static FileUploadServiceGrpc.FileUploadServiceStub noBlockStub;


    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("MENU");
            System.out.println(" 1 - Send Info");
            System.out.println(" 99 - Exit");
            //System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 1) || op == 99));
        return op;
    }


    private static String readString(String msg) {
        Scanner scaninput = new Scanner(System.in);
        System.out.println(msg);
        return scaninput.nextLine();
    }


    public static void main(String[] args) {
        //args[0] server ip
        //args[1] server port
        try {
            if (args.length == 2) {
                svcIP = args[0];
                svcPort = Integer.parseInt(args[1]);
            }
            channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            blockingStub = FileUploadServiceGrpc.newBlockingStub(channel);
            noBlockStub = FileUploadServiceGrpc.newStub(channel);

          // String filepath="D:\\Disciplinas\\ComputacaoNuvemv2021\\MixServices\\ClientApp\\Sardinhas.jpg";
          // blockingStub.test(TestString.newBuilder().setStr(filepath).build());

           sendFile(readString("Qual o ficheiro"),readString("Qual o nome do blob"));


           readString("prima enter");


            channel.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void sendFile(String absFileName, String blobName) throws IOException {

        StreamObserver<Contents> sendBlocksStream=noBlockStub.sendFileBlocks(new StreamObserver<Result>() {
            @Override
            public void onNext(Result result) {
                System.out.println(result.getHashId()+ " with "+result.getNumBlocksReceived()+ "blocks");
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error"+throwable.getMessage());
            }
            @Override
            public void onCompleted() {      }
        });

        Path uploadFrom = Paths.get(absFileName);
        String contentType = Files.probeContentType(uploadFrom);
        byte[] buffer=new byte[64*1024];
        try (InputStream input = Files.newInputStream(uploadFrom)) {
            int limit;
            while ((limit = input.read(buffer)) >= 0) {
                Contents msg = Contents.newBuilder().setFilename(blobName)
                        .setFileBlockBytes(ByteString.copyFrom(buffer))
                        .setContentType(contentType)
                        .build();
                sendBlocksStream.onNext(msg);
            }
            sendBlocksStream.onCompleted();
        }
    }


}
