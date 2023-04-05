package demospread;

import spread.BasicMessageListener;
import spread.SpreadConnection;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.util.Scanner;

public class MessageHandling implements BasicMessageListener {
    private SpreadConnection connection;

    public MessageHandling(SpreadConnection connection) {
        this.connection = connection;
    }

    @Override
    public void messageReceived(SpreadMessage spreadMessage) {
        try {
            PrintMessages.MessageDetails(spreadMessage);
            // enviar reply direto para o sender se a mensagem r«tiver conteudo "request"
            if (!spreadMessage.isMembership()) {
                SpreadGroup myPrivateGroup = connection.getPrivateGroup();
                //System.out.println("myPrivateGroup=" + myPrivateGroup.toString());
                SpreadGroup senderPrivateGroup = spreadMessage.getSender();
                //System.out.println("senderPrivateGroup=" + senderPrivateGroup.toString());
                if (!myPrivateGroup.equals(senderPrivateGroup)) {
                    String txtMsg = new String(spreadMessage.getData());
                    if (txtMsg.toLowerCase().equals("request")) {
                        SpreadMessage msg = new SpreadMessage();
                        msg.setSafe();
                        msg.addGroup(senderPrivateGroup.toString());
                        msg.setData(("Hello i am " + myPrivateGroup.toString() + ":I was received your group request").getBytes());
                        //System.out.println("enviar reply direto");
                        connection.multicast(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

