package service;


import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.*;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;

import java.io.IOException;

public class PubSub implements MessageReceiver {

    static void Publish(String projID, Document doc) {
        TopicName topic = TopicName.ofProjectTopicName(projID, "test-mixservices");
        Publisher publisher = null;
        try {
            publisher = Publisher.newBuilder(topic).build();
            String jsonstring=new Gson().toJson(doc);

            ByteString msgData = ByteString.copyFromUtf8(jsonstring);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(msgData)
                    .build();
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            String msgID = future.get();
            System.out.println("Message Published with ID=" + msgID);
            publisher.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static Subscriber subscriber = null;
    static void Subscribe(String projID) {
        ProjectSubscriptionName projsubscriptionName = ProjectSubscriptionName.of(
                projID, "test-mixservices-sub");
         subscriber =
                Subscriber.newBuilder(projsubscriptionName, new PubSub())
                        .build();
        subscriber.startAsync().awaitRunning();

    }

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        String jsonString=pubsubMessage.getData().toStringUtf8();
        Document doc=new Gson().fromJson(jsonString, Document.class);
        System.out.println("PubSub msg received with doc ID "+doc.docID+":");
        doc.labels.forEach((s) -> {System.out.println("label:"+s);});
        ackReplyConsumer.ack();
    }

    public static void listTopics(String projID) throws IOException {
        TopicAdminClient topicAdmin = TopicAdminClient.create();
        TopicAdminClient.ListTopicsPagedResponse res = topicAdmin.listTopics(ProjectName.of(projID));
        for (Topic top : res.iterateAll()) {
            System.out.println("TopicName=" + top.getName());
        }
        topicAdmin.close();
    }
}
