package svcrest;

import org.slf4j.Logger;
import org.slf4j.impl.SimpleLoggerFactory;

import java.io.IOException;

public class ServiceRest {
    static Logger logger=new SimpleLoggerFactory().getLogger("ServiceRestlogs");

    public static void main(String[] args) {
        try {
            LOG.InitLogs("ServiceRestlogs",false);
             spark.Spark.port(7500);

        spark.Spark.get("/ping", (req, res) -> {
            LOG.INFO("/ping");
            return "Service is alive on port 7500";
        });
        spark.Spark.get("/hello/:name", (request, response) -> {
            String name=request.params(":name");
            LOG.INFO("/hello/"+name);
            return "Hello " + name;
            //return "Hello: " + request.params(":name");
        });
        spark.Spark.get("/calc/:expression", (request, response) -> {
            return calc(request.params(":expression"));
        });
    } catch (Exception e) {
        LOG.INFO("Unhandled exception");
    }
    }

    static String calc(String expression) {
     try {
         LOG.INFO("/calc/"+expression);
        String[] opers;
        int op1;
        int op2;
        int res;
        if (expression.split("[+]").length == 2) { // add
            opers=expression.split("[+]");
            op1= Integer.parseInt(opers[0]);
            op2= Integer.parseInt(opers[1]);
            res=op1+op2;
            return "Result: "+res;
        }
         if (expression.split("[-]").length == 2) { // sub
             opers=expression.split("[-]");
             op1= Integer.parseInt(opers[0]);
             op2= Integer.parseInt(opers[1]);
             res=op1-op2;
             return "Result: "+res;
         }
         if (expression.split("[*]").length == 2) { // sub
             opers=expression.split("[*]");
             op1= Integer.parseInt(opers[0]);
             op2= Integer.parseInt(opers[1]);
             res=op1*op2;
             return "Result: "+res;
         }
         return "Expresion with invalid operator";
        } catch(Exception ex ) {
           return "Expression Error: Use operand2{+,-,*}operand2";
     }
    }

}
