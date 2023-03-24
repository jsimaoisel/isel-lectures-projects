package service;



import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetectTranslateAPIs {

    static void testVisionLabels(String filePath) {

        //String filePath = "path/to/your/image/file.jpg";
        try {
            detectLabels(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> detectLabels(String file) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        // ByteString imgBytes = ByteString.readFrom(new FileInputStream(file));
        //Image img = Image.newBuilder().setContent(imgBytes).build();
        // obtém imagem from Storage using gs URI
        Image img = Image.newBuilder()
                .setSource(ImageSource.newBuilder().setImageUri("gs://cn2122-testmixservices/" + file).build())
                .build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                } else {
                    // For full list of available annotations, see http://g.co/cloud/vision/docs
                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                        labels.add(annotation.getDescription());
//                        annotation.getAllFields()
//                                .forEach((k, v) -> System.out.format("%s : %s%n", k, v.toString()));
                    }
                }
            }
        }
        return labels;
    }

    static List<String> TranslateLabels(List<String> labels) {
        List<String> labelsTranslated=null;
        try {
            Translate translate = TranslateOptions.getDefaultInstance().getService();
             labelsTranslated = new ArrayList<>();
            for (String label : labels) {
                Translation translation = translate.translate(
                        label,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("pt"));
                labelsTranslated.add(translation.getTranslatedText());

            }

        } catch (Exception ex) {
              ex.printStackTrace();
        } finally {
            return labelsTranslated;
        }
    }


}
