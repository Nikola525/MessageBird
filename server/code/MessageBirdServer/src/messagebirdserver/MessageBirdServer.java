/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messagebirdserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import networkpacket.NetworkPacket;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Wang
 */
public class MessageBirdServer {
    List<ClientIPAddress> clientipaddresslist;
    List<NetworkPacket> pktlist;
    ObjectInputStream serverstaterestore = null;
    ObjectOutputStream serverstatesave = null;
    static long currentUsrIdNum = 30000;
   
    
    static class ServetState implements Serializable{
        long currentUsrIdNum = 0;
    }
    


    /**
     * @param args the command line arguments
     */
    
    public MessageBirdServer(){
        clientipaddresslist = new ArrayList<>();
        pktlist = new ArrayList<>();
        try{
            serverstaterestore = new ObjectInputStream(new FileInputStream(new File("serverstate")));
            ServetState state = (ServetState)serverstaterestore.readObject();
            currentUsrIdNum = state.currentUsrIdNum; 
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    static class Account implements Serializable{
        public long id;
        public String email;
        public String password;
        public String name;
        //public List<Long> friends;
    }
    class ClientIPAddress{
        public ClientIPAddress(long i,Socket s){
            id = i;
            socket = s;
            try{
                osw = new OutputStreamWriter(socket.getOutputStream());
                bufferedwriter = new BufferedWriter(osw);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        public long id;
        public Socket socket;
        public OutputStreamWriter osw;
        public BufferedWriter bufferedwriter;
    }
    
    public void sendMsgToThePartner(NetworkPacket pkt){
        try{
            for(int i=0;i<clientipaddresslist.size();++i){
                System.out.println("finding the partner "+ clientipaddresslist.get(i).id +
                        "( " + pkt.to + "needed）\n");
                if(clientipaddresslist.get(i).id == pkt.to){
                    JSONObject obj = new JSONObject();
                    obj.put("type", String.valueOf(NetworkPacket.TYPE_NEWMSG));
                    obj.put("content", pkt.content);
                    obj.put("to", String.valueOf(pkt.to));
                    obj.put("from", String.valueOf(pkt.from));
                    BufferedWriter bw = null;
                    bw = clientipaddresslist.get(i).bufferedwriter;
                    bw.write(obj.toString() + "\n");
                    System.out.print("Type: NewMsg" + "From:" + pkt.from + 
                            "To: " + pkt.to + "Content: " + pkt.content + "\n");
                    bw.flush();
                }
            }
            System.out.println("the pkt from " + pkt.from + " to " + pkt.to
                    + "(" + pkt.content + ") has saved in the server's pool");
            pktlist.add(pkt);
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
    public void consumeTheMsgService(ClientIPAddress addr){
        clientipaddresslist.add(addr);
    }
    public void exitTheMsgService(ClientIPAddress addr){
        clientipaddresslist.remove(addr);
    }
    
    
    
    int port = 8000;
    public InputStream input= null;
    public OutputStream output= null;
    
    public void run(){
        try{
            System.out.println("start to listen.");
            ServerSocket server = new ServerSocket(port);
            while(true){
               System.out.println("wait the next client.");
               Socket socket = server.accept();
               System.out.println("create thread.");
               Thread serveClient = new Thread(new ServeClient(socket));
               serveClient.start();
               serverstatesave = new ObjectOutputStream(new FileOutputStream(new File("serverstate")));
               ServetState state = new ServetState();
               state.currentUsrIdNum = currentUsrIdNum;
               serverstatesave.writeObject(state);
           
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private class ServeClient implements Runnable{
        public ServeClient(Socket s){
            socket = s;
            islogouted = false;
        }
        boolean islogouted = false;
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        NetworkPacket rcvpkt;
        NetworkPacket sndpkt;
        OutputStreamWriter osw;
        BufferedWriter bufferedwriter;
        
        
        ObjectOutputStream obj_usr_registed = null;
        @Override
        public void run(){
            try{
                System.out.println(new String("serve ") + socket.getInetAddress().getHostName());
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                osw = new OutputStreamWriter(socket.getOutputStream());
                bufferedwriter = new BufferedWriter(osw);
                
                while(!islogouted){

                    
                    
                    
                    rcvpkt = (NetworkPacket)ois.readObject();
                    NetworkPacket sndpkt = new NetworkPacket();
                    int type = (int)rcvpkt.type;
                    switch(type){
                        case NetworkPacket.TYPE_REGISTER:
                            
                            sndpkt.to = currentUsrIdNum;
                            sndpkt.state = 0;
                            sndpkt.type = NetworkPacket.TYPE_REGISTER;
                            oos.writeObject(sndpkt);
                            System.out.println(rcvpkt.type + rcvpkt.name + 
                                    rcvpkt.email + rcvpkt.password);
                            obj_usr_registed = new ObjectOutputStream(
                                    new FileOutputStream(new File(String.valueOf(currentUsrIdNum))));
                            
                            Account account = new Account();
                            account.id = currentUsrIdNum;
                            account.email = rcvpkt.email;
                            account.name = rcvpkt.name;
                            account.password = rcvpkt.password;
                            obj_usr_registed.writeObject(account);
                            currentUsrIdNum++;
                            break;
                        case NetworkPacket.TYPE_MESSAGE:
                            System.out.println("Type：" + rcvpkt.type + 
                                    "From " +  rcvpkt.from + 
                                    "To " + rcvpkt.to + 
                                    "Content " + rcvpkt.content + "\n");
                            sendMsgToThePartner(rcvpkt);
                            break;
                        case NetworkPacket.TYPE_LOGIN:
                            
                            try{
                                ObjectInputStream usraccount = new ObjectInputStream(new FileInputStream(new File(String.valueOf(rcvpkt.from))));
                                Account account1 = (Account)usraccount.readObject();
                                if(account1.password.equals(rcvpkt.password)){
                                sndpkt.state = NetworkPacket.STATE_SUCCESS;
                                }else{
                                    sndpkt.state = NetworkPacket.STATE_FAILED;
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                sndpkt.state = NetworkPacket.STATE_FAILED;
                            }

                            
                            sndpkt.type = NetworkPacket.TYPE_LOGIN;
                            oos.writeObject(sndpkt);
                            System.out.println(rcvpkt.type + rcvpkt.from + 
                                    rcvpkt.password + rcvpkt.content);
                            consumeTheMsgService(new ClientIPAddress(rcvpkt.from, socket));
                            break;
                        case NetworkPacket.TYPE_ACKNOWLEDGE:
                            System.out.println(rcvpkt.type + rcvpkt.from);
                            break;
                        case NetworkPacket.TYPE_ADDPARTNER:
                            System.out.println("Type：" + rcvpkt.type + 
                                    "From " +  rcvpkt.from + 
                                    "To " + rcvpkt.to + 
                                    "Content " + rcvpkt.content + "\n");
                            JSONObject obj = new JSONObject();
                            obj.put("type", String.valueOf(NetworkPacket.TYPE_ADDPARTNER));
                            obj.put("content", "XXX");
                            obj.put("to", String.valueOf(rcvpkt.from));
                            obj.put("from", String.valueOf(rcvpkt.to));
                            bufferedwriter.write(obj.toString() + "\n");
                            bufferedwriter.flush();
                            break;
                        case NetworkPacket.TYPE_LOGOUT:
                            exitTheMsgService(new ClientIPAddress(rcvpkt.from, socket));
                            System.out.println(rcvpkt.type + rcvpkt.from);
                            islogouted = true;
                            break;
                    }
                    
                    //System.out.println(rcvpkt.type + rcvpkt.content);
                    //oos.writeObject(rcvpkt);
                    
                    /*
                    Thread.sleep(10000);
                    System.out.println("write 100 to socket.");
                    sndpkt = new NetworkPacket();
                    sndpkt.email = new String("email");
                    sndpkt.name = new String("name");
                    sndpkt.password = new String("Password");
                    sndpkt.state = 100;
                    sndpkt.to = 3;
                    sndpkt.type = 4;
                    sndpkt.content = new String("hi.client.");
                    oos.writeObject(sndpkt);
                    System.out.println(oos.toString());
                  */
                    
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
    }

    
    public static void main(String[] args) {
        // TODO code application logic here
        new MessageBirdServer().run();

    }
    
    
    
}
