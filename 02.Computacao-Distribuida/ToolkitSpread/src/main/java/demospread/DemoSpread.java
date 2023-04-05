package demospread;

import spread.SpreadConnection;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.util.List;
import java.util.Scanner;

public class DemoSpread {


    //private static String daemonIP="localhost";
    private static String daemonIP="34.65.196.247";
    //private static String daemonIP="34.65.138.224";
    // VM with spreaad
    //private static String daemonIP="146.148.24.143"; // Node1
    //private static String daemonIP="35.195.61.177"; // Node2

    private static int daemonPort=4803;

    public static void main(String[] args) {
        try {
            if (args.length > 0 ) {
                daemonIP=args[0];
            }
            Scanner scaninput = new Scanner(System.in);
            String userName = read("MemberApp name? ", scaninput);
            GroupMember member = new GroupMember(userName, daemonIP, daemonPort);

            boolean end=false;
            while (!end) {
                int option = Menu();
                switch (option) {
                    case 1:  // join group
                        String groupName = read("Join to group named? ", scaninput);
                        member.JoinToGrupo(groupName);
                        break;
                    case 2: // send message
                        String groupToSend=read("Group Name to send Message? ", scaninput);
                        String txtMessage=read("Message text (Data)? ",scaninput);
                        member.SendMessage(groupToSend,txtMessage);
                        break;
                    case 3:
                        List<String> groupsBelonging=member.getNamesOfBelongingGroups();
                        // Leave a group.
                        if (groupsBelonging.size() == 0) {
                            System.out.println("No group to leave.");
                            break;
                        }
                        for (String gname : groupsBelonging)
                            System.out.println("Joined to: " + gname + ".");
                        String nameToLeave=read("Group name to leave",scaninput);
                        member.groupLeave(nameToLeave);

                        break;
                    case 99:
                        end = true;
                        break;
                }
            }
            member.close();
            System.exit(0);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private  static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Join to a Group");
            System.out.println(" 2 - Send Message to Group");
            System.out.println(" 3 - Leave from a Group");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 3) || op == 99));
        return op;
    }

    private static String read(String msg, Scanner scaninput) {
        System.out.println(msg);
        String aux=scaninput.nextLine();
        return aux;
    }


}



