package functionpubsub;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.functions.*;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Entrypoint  implements BackgroundFunction<PSmessage> {
    private static final Logger logger = Logger.getLogger(Entrypoint.class.getName());
    private static Firestore db = initFirestore();

    private static Firestore initFirestore() {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            FirestoreOptions options = FirestoreOptions.newBuilder().setCredentials(credentials).build();
            Firestore db = options.getService();
            return db;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void accept(PSmessage message, Context context) throws Exception {
        if (db == null) {
            logger.info("Error connecting to Firestore. Exiting function.");
            //https://cloud.google.com/functions/docs/bestpractices/retries#why_event-driven_functions_fail_to_complete
            //https://cloud.google.com/functions/docs/bestpractices/retries#set_an_end_condition_to_avoid_infinite_retry_loops
            throw new RuntimeException("Error connecting to Firestore");
        }
        logger.info("original message " + message.data);
        String data = new String(Base64.getDecoder().decode(message.data));
        logger.info(data);
        CollectionReference colRef = db.collection("CFPubSubMessages");
        // O message ID vem no eventID
        DocumentReference docRef = colRef.document(""+context.eventId());
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("msg-data", message.data);
        map.put("data", data);
        if (data.compareTo("error") == 0)
           throw new Exception("error forced from data");
        map.put("ctx-messageId", context.eventId());
        map.put("ctx-pubTime", context.timestamp());
        ApiFuture<WriteResult> result = docRef.set(map);
        result.get();
        logger.info("Event was written to Firestore.");
    }


}
