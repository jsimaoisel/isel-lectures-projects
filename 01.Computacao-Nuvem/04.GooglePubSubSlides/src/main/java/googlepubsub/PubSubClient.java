package googlepubsub;


import com.google.api.core.ApiFuture;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;

import java.io.IOException;
import java.util.Scanner;

public class PubSubClient {
    static String PROJECT_ID;

    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 0 - List Project Topics");
            System.out.println(" 1 - Create Topic");
            System.out.println(" 2 - Publish One message");
            System.out.println(" 3 - Publish multiple messages");
            System.out.println(" 4 - List Subscriptions on Project");
            System.out.println(" 5 - Create Subscription");
            System.out.println(" 6 - Subscribe messages");
            System.out.println(" 7 - UnSubscribe messages");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 0 && op <= 7) || op == 99));
        return op;
    }

    private static String read(String msg, Scanner input) {
        System.out.println(msg);
        return input.nextLine();
    }

    // Menu options
    public static void listTopics() throws IOException {
        TopicAdminClient topicAdmin = TopicAdminClient.create();
        TopicAdminClient.ListTopicsPagedResponse res = topicAdmin.listTopics(ProjectName.of(PROJECT_ID));
        for (Topic top : res.iterateAll()) {
            System.out.println("TopicName=" + top.getName());
        }
        topicAdmin.close();
    }

    public static void createNewTopic(String topicName) throws IOException {
        TopicAdminClient topicAdmin = TopicAdminClient.create();
        //ProjectTopicName projTopName=ProjectTopicName.of(PROJECT_ID, topicName);
        TopicName projTopName = TopicName.ofProjectTopicName(PROJECT_ID, topicName);
        topicAdmin.createTopic(projTopName);
        topicAdmin.close();
    }

    public static void publishMessage(String pubTopicName, String msg) throws Exception {
        TopicName topic = TopicName.ofProjectTopicName(PROJECT_ID, pubTopicName);
        Publisher publisher = Publisher.newBuilder(topic).build();
        ByteString msgData = ByteString.copyFromUtf8(msg);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
            .setData(msgData)
            .putAttributes("key1", "value1")
            .build();
        ApiFuture<String> future = publisher.publish(pubsubMessage);
        String msgID = future.get();
        System.out.println("Message Published with ID=" + msgID);
        publisher.shutdown();
    }

    public static void publishMultipleMessages(String topicName, int numMsg, String msgPrefix) throws Exception {
        TopicName topic = TopicName.ofProjectTopicName(PROJECT_ID, topicName);
        Publisher publisher = Publisher.newBuilder(topic).build();
        for (int i = 0; i < numMsg; i++) {
            ByteString msgData = ByteString.copyFromUtf8("Multiple:" + msgPrefix + (i + 1));
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(msgData)
                .build();
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            String msgID = future.get();
            System.out.println("Message Published with ID=" + msgID);
        }
        publisher.shutdown();
    }

    public static void listExistingSubscriptions() throws IOException {
        SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create();
        SubscriptionAdminClient.ListSubscriptionsPagedResponse list = subscriptionAdminClient.listSubscriptions(ProjectName.of(PROJECT_ID));
        System.out.println("Existing Subscriptions:");
        for (Subscription sub : list.iterateAll()) {
            System.out.println(sub.getName() + " (Topic:" + sub.getTopic() + ")");
        }
        subscriptionAdminClient.close();
    }

    public static void createSubscription(String subtopicName, String subName) throws IOException {
        TopicName topicName = TopicName.ofProjectTopicName(PROJECT_ID, subtopicName);
        //ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(
        SubscriptionName subscriptionName = SubscriptionName.of(
            PROJECT_ID, subName);
        SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create();
        PushConfig pConfig = PushConfig.getDefaultInstance();
        subscriptionAdminClient.createSubscription(subscriptionName, topicName, pConfig, 0);
        subscriptionAdminClient.close();
    }

    public static Subscriber subscribeMessages(String projectID, String subscriptionName) {
        ProjectSubscriptionName projSubscriptionName = ProjectSubscriptionName.of(
            projectID, subscriptionName);
        ExecutorProvider executorProvider = InstantiatingExecutorProvider
                .newBuilder()
                .setExecutorThreadCount(1) // um só thread no handler
                .build();
        Subscriber subscriber =
            Subscriber.newBuilder(projSubscriptionName, new MessageReceiveHandler())
                .build();
        subscriber.startAsync().awaitRunning();
        return subscriber;
    }

    public static void main(String[] args) {
        // - Set environment variable
        //     GOOGLE_APPLICATION_CREDENTIALS="pathname to AccountServiceKEY.json"
        // - set project_id as a command line argument
        try {
            if (args.length > 0)
                PROJECT_ID = args[0];
            else {
                System.out.println("pass project id as a command line argument");
                System.exit(-1);
            }
            System.out.println("ProjectID = " + PROJECT_ID);
            Subscriber subscriber = null;
            Scanner scanInput = new Scanner(System.in);
            boolean end = false;
            while (!end) {
                int option = Menu();
                switch (option) {
                    case 0:
                        listTopics();
                        break;
                    case 1:
                        String newTopicName = read("Topic name to create?", scanInput);
                        createNewTopic(newTopicName);
                        break;
                    case 2:
                        String pubTopicName = read("Topic name to Publish?", scanInput);
                        String msg = read("Message to Publish?", scanInput);
                        publishMessage(pubTopicName, msg);
                        break;
                    case 3:
                        String pubTopicName2 = read("Topic name to Publish?", scanInput);
                        String nMsg = read("Number of messages to Publish?", scanInput);
                        String msgPrefix = read("Message prefix to Publish?", scanInput);
                        int numMsg = Integer.parseInt(nMsg);
                        publishMultipleMessages(pubTopicName2, numMsg, msgPrefix);
                        break;
                    case 4:
                        listExistingSubscriptions();
                        break;
                    case 5:
                        String subtopicName = read("Topic to create Subscription?", scanInput);
                        String subName = read("Subscription name?", scanInput);
                        createSubscription(subtopicName, subName);
                        break;
                    case 6:
                        String subscriptionName = read("Subscription name ?", scanInput);
                        String projectID = PROJECT_ID;
                        subscriber = subscribeMessages(projectID, subscriptionName);
                        break;
                    case 7:
                        if (subscriber != null) {
                            subscriber.stopAsync();
                            subscriber.awaitTerminated();
                        }
                        break;
                    case 99:
                        end = true;
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
