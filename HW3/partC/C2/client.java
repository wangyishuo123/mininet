/**
 * Created by Yishuo on 2017/4/17.
 */
import java.net.*;
import java.io.*;
public class client {
    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        String node = args[2];//H1 H2 R1 R2 R3 R4
        try
        {
            System.out.println("Connecting to " + serverName
                    + " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out =
                    new DataOutputStream(outToServer);

            out.writeUTF(node + " " + client.getLocalSocketAddress()
                    + "say Hello.");



            InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
            System.out.println("Server says " + in.readUTF());
            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }
}
