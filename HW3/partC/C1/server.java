/**
 * Created by Yishuo on 2017/4/17.
 */
import java.net.*;
import java.io.*;
public class server extends Thread{
    private ServerSocket serverSocket;
    static String node;
    public server(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

    public void run()
    {
        while(true)
        {
            try
            {
//                File filename = new File("beginning_table.txt");
//                InputStreamReader reader = new InputStreamReader(
//                        new FileInputStream(filename));
//                BufferedReader br = new BufferedReader(reader);
//                String line = "";
//                line = br.readLine();
//                while (line != null) {
//                    String eachLine[] = line.split(" ");
//                    System.out.println(node + "Server Beginning: ");
//                    if(eachLine[1] == node){
//                        System.out.println(eachLine[0] + " " + eachLine[1] + " " + eachLine[2]);
//                    }
//                }

////////////////////////////////////////////////////////////////////////


                System.out.println("Waiting for client...");

                Socket server = serverSocket.accept();
                System.out.println("Just connected to "
                        + server.getRemoteSocketAddress());
                DataInputStream in =
                        new DataInputStream(server.getInputStream());
                String str = in.readUTF();
                System.out.println(str);

                String fromNode = str.substring(0,2);
                //in.readUTF() is the message get from the client.
                Graph.build_Graph(fromNode, node);
                System.out.println("PASS");
                DataOutputStream out =
                        new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to "
                        + server.getLocalSocketAddress()
                        + "\nWriting table successfully");
                server.close();
            }catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
    public static void main(String [] args)
    {
        int port = Integer.parseInt(args[0]);
        node = args[1];//H1 H2 R1 R2 R3 R4
        try
        {
            Thread t = new server(port);
            t.start();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

