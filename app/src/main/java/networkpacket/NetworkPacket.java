package networkpacket;

public class NetworkPacket implements java.io.Serializable{
    public NetworkPacket(){

    }
    public long type;
    public long to;
    public String content;
    public String email;
    public String name;
    public String password;
    public long state;
    public static final long TYPE_LOGIN = 21;
}