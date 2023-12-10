package clientapp;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Timestamp;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import rpcstubs.*;
import rpcstubs.News;
import rpcstubs.Void;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Client {

   // private static String svcIP = "localhost";
    private static String svcIP = "34.38.77.35";
    private static int svcPort = 8000;
    private static ManagedChannel channel;
    private static BaseServiceGrpc.BaseServiceBlockingStub blockingStub;
    private static BaseServiceGrpc.BaseServiceStub noBlockStub;
    private static BaseServiceGrpc.BaseServiceFutureStub futStub;

    static void case1() throws InterruptedException {
        // sincrono
        System.out.println("\nCase1 Unário blocking stub:");
        for (int i = 0; i < 3; i++) {
            Request req = Request.newBuilder().setReqID(i).setTxt("request " + i).build();
            Reply rply = blockingStub.case1(req);
            System.out.println("Reply(" + rply.getRplyID() + "):" + rply.getTxt());
        }
        // assíncrono
        System.out.println("Case1 with no blocking Stub");
        Request req = Request.newBuilder().setReqID(100).setTxt("request assincrono ").build();
        ClientStreamObserver replyStreamObserver = new ClientStreamObserver();
        noBlockStub.case1(req, replyStreamObserver);
        while (!replyStreamObserver.isCompleted()) {
            System.out.println(" cliente active");
            Thread.sleep(1 * 1000);
        }
        List<Reply> replies = replyStreamObserver.getReplays();
        if (replyStreamObserver.OnSuccesss()) {
            for (Reply rpy : replyStreamObserver.getReplays()) {
                System.out.println("Reply for Case1 call:" + rpy.getRplyID() + ":" + rpy.getTxt());
            }
        }
        // future
        System.out.println("Case1 with Future");
        Reply frpy = null;
        try {
            Request futRequest = Request.newBuilder().setReqID(200).setTxt("invoked with future").build();
            //ListenableFuture<OperationReply> list=new CalcService.
            ListenableFuture<Reply> fut = futStub.case1(futRequest);
            while (!fut.isDone()) {
                System.out.println("waiting futures completed");
                Thread.sleep(1 * 1000);
            }
            frpy = fut.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Future reply:RES=" + frpy.getTxt());
    }

    static void case2() throws InterruptedException {
        //sincrono
        System.out.println("\nCase2 stream server blocking stub:");
        Request req = Request.newBuilder().setReqID(5).setTxt("request case2").build();
        Iterator<Reply> manyRpys = blockingStub.case2(req);
        while (manyRpys.hasNext()) {
            Reply rpy = manyRpys.next();
            System.out.println("Reply for Case2 BlockStub:" + rpy.getRplyID() + ":" + rpy.getTxt());
        }
        //assíncrono
        System.out.println("Case2 with no blocking Stub");
        ClientStreamObserver rpyStreamObs = new ClientStreamObserver();
        noBlockStub.case2(req, rpyStreamObs);
        while (!rpyStreamObs.isCompleted()) {
            System.out.println("Active and waiting for Case2 completed ");
            Thread.sleep(1 * 1000);
        }
        if (rpyStreamObs.OnSuccesss()) {
            for (Reply rpy : rpyStreamObs.getReplays()) {
                System.out.println("Reply for Case2 call:" + rpy.getRplyID() + ":" + rpy.getTxt());
            }
        }
    }

    static void case3() throws InterruptedException {
        System.out.println("\nCase3 stream client with no blocking Stub");
        ClientStreamObserver rpyStreamObs = new ClientStreamObserver();
        StreamObserver<Request> reqs = noBlockStub.case3(rpyStreamObs);
        for (int i = 0; i < 5; i++) {
            Request req = Request.newBuilder().setReqID(i).setTxt("request case3 " + i).build();
            reqs.onNext(req);
        }
        reqs.onCompleted();
        while (!rpyStreamObs.isCompleted()) {
            System.out.println("cliente active");
            Thread.sleep(1 * 1000);
        }
        // processar a unica resposta em rpyStreamObs.getReplays()
    }

    static void case4() throws InterruptedException {
        System.out.println("\nCase4 stream client and stream server with no blocking Stub");
        ClientStreamObserver rpyStreamObs = new ClientStreamObserver();
        StreamObserver<Request> reqs = noBlockStub.case4(rpyStreamObs);
        for (int i = 0; i < 5; i++) {
            Request req = Request.newBuilder().setReqID(i).setTxt("request case4 " + i).build();
            reqs.onNext(req);
        }
        reqs.onCompleted();
        while (!rpyStreamObs.isCompleted()) {
            System.out.println(" cliente active");
            Thread.sleep(1 * 1000);
        }
        // Senão tiver sido processada cada resposta em rpyStreamObs.OnNext()
        // processar todas as respostas em rpyStreamObs.getReplays()
    }

    static void publishNews() {
        long millis = System.currentTimeMillis();
        Date dt = new Date(millis);
        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        System.out.println("Milliseconds to Date: " + df.format(dt));
        Timestamp ts = Timestamp.newBuilder().setSeconds(millis / 1000).build();
        blockingStub.publishNews(News.newBuilder().setTs(ts).setTexto("tempo chuvoso").build());
    }

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                svcIP = args[0];
                svcPort = Integer.parseInt(args[1]);
            }
            System.out.println("connect to "+svcIP+":"+svcPort);
            channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
            blockingStub = BaseServiceGrpc.newBlockingStub(channel);
            noBlockStub = BaseServiceGrpc.newStub(channel);
            futStub = BaseServiceGrpc.newFutureStub(channel);
            long starttime = System.currentTimeMillis();
            // ping Server
            Reply ping = blockingStub.pingServer(Void.newBuilder().build());
            System.out.println(ping.getTxt());
            long endtime = System.currentTimeMillis();
            System.out.println("Ping Operation completed in: " + (endtime - starttime) + " ms");
            // invocar as operações disponibilizadas pelo serviço
            Scanner scanInput = new Scanner(System.in);
            boolean endmenu = false;
            while (!endmenu) {
                int option = Menu();
                switch (option) {
                    case 1:
                        case1();
                        break;
                    case 2:
                        case2();
                        break;
                    case 3:
                        case3();
                        break;
                    case 4:
                        case4();
                        break;
                    case 5:
                        publishNews();
                        break;
                    case 99:
                        endmenu = true;
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Case1 - chamada unária");
            System.out.println(" 2 - Case 2 - chamada com sream de servidor");
            System.out.println(" 3 - Case 3 - chamada com stream de cliente");
            System.out.println(" 4 - stream de cliente e de servidor");
            System.out.println(" 5 - Publish news (chamada unária)");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 5) || op == 99));
        return op;
    }

    private static String read(String msg, Scanner input) {
        System.out.println(msg);
        return input.nextLine();
    }
}
