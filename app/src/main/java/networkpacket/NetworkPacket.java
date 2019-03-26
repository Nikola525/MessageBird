package networkpacket;

public class NetworkPacket implements java.io.Serializable{
    public NetworkPacket(){

    }
    public long type;
    public long to;
    public long from;
    public String content;
    public String email;
    public String name;
    public String password;
    public long state;
    public static final int STATE_SUCCESS  = 0;
    public static final int STATE_FAILED  = 300;
    public static final int TYPE_LOGIN = 321;
    public static final int TYPE_REGISTER = 323;
    public static final int TYPE_MESSAGE = 325;
    public static final int TYPE_ACKNOWLEDGE = 327;
    public static final int TYPE_NEWMSG = 329;
    public static final int TYPE_LOGOUT = 331;
    public static final int TYPE_ADDPARTNER = 333;

}