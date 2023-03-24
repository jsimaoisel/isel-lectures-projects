package functionhttp;

import com.google.cloud.compute.v1.*;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

// mvn function:run -DGOOGLE_APPLICATION_CREDENTIALS="D:\Disciplinas\ComputacaoNuvemv2122\RepositCN\CN\14.1.ExemploHttpBase\cn2122-jsla-geral-1a82a44cea87-v1.json"


import java.io.BufferedWriter;

public class Entrypoint implements HttpFunction {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();

        String projectID = "cn2122-jsla-geral";
        String zone = request.getFirstQueryParameter("zone").orElse("europe-west1-b");
        writer.write("List running Vms in zone="+zone+"\n");
        try (InstancesClient client = InstancesClient.create()) {
            for (Instance instance : client.list(projectID, zone).iterateAll()) {
                if (instance.getStatus().compareTo("RUNNING") == 0) {
                    writer.write("Name: " + instance.getName() +"\n");
                    String ip = instance.getNetworkInterfaces(0).getAccessConfigs(0).getNatIP();
                    writer.write("    Last Start time: " + instance.getLastStartTimestamp()+"\n");
                    writer.write("    IP: " + ip+"\n");
                }
            }
        }
    }
}
