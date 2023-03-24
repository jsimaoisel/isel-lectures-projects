package functionhttp;


import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entrypoint implements HttpFunction {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        String name=request.getFirstQueryParameter("name").orElse("World");
//        Map<String, List<String>> pars=request.getQueryParameters();
//        for (String key : pars.keySet())
//            writer.write("parname"+key+" ,"+pars.get(key).get(0));
        writer.write("Hello "+name+" from Cloud Function triggered by http");
    }
}
