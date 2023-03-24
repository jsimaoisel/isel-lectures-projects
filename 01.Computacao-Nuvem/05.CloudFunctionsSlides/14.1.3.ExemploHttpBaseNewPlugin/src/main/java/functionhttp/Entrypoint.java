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
//        String name=request.getFirstQueryParameter("name").orElse("World");
//        writer.write("Hello "+name+" !");

        // obter os ips do instance group
        String projectID = "cn2122-jsla-geral";
        String zone = "europe-west1-b";

        String groupName = request.getFirstQueryParameter("groupname").orElse(" ");
        InstanceGroupManagersClient managersClient = InstanceGroupManagersClient.create();
        try (InstancesClient client = InstancesClient.create()) {
            for (Instance curInst : client.list(projectID, zone).iterateAll()) {
                if (curInst.getName().contains(groupName)) {
                    writer.write("Name: " + curInst.getName() + "  VMId:" + curInst.getId()+"\n");
                    writer.write("    Number of network interfaces: " + curInst.getNetworkInterfacesCount()+"\n");
                    String ip = curInst.getNetworkInterfaces(0).getAccessConfigs(0).getNatIP();
                    writer.write("    IP: " + ip+"\n");
                    writer.write("    Status: " + curInst.getStatus() + " : Last Start time: " + curInst.getLastStartTimestamp()+"\n");
                }
            }
        }
    }
}
