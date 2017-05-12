
import java.io.*;
import java.net.Socket;

public final class Ipv6Client {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("codebank.xyz", 38004)) {
            System.out.println("Connected to server.");
            
            //Obtain IP address of server
            String address = socket.getInetAddress().getHostAddress();
            byte[] addr = socket.getInetAddress().getAddress();

            //Initialize an input stream and an output stream to communicate with server
            InputStream is = socket.getInputStream();

            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os, true, "UTF-8");

            //holds the data that will hold the bytes to each packet
            byte[] b = new byte[4136];

            //holds the least amount of bytes a header can contain
            int nBytes = 40;
            
            //sets the fixed bytes of Ipv6 header
            //bytes for Version, Traffic Class
            b[0] = 96;
            b[1] = 0;
            b[2] = 0;
            b[3] = 0;

            //byte values for Next Header set as UDP
            b[6] = 17;

            //byte value for Hop Limit 
            b[7] = 20;

            //bytes hold the Source addr
            //bytes 8 to 23
            //bytes 8 to 17 set to 0;
            b[18] = (byte)255;
            b[19] = (byte)255;
            b[20] = 0;
            b[21] = 0;
            b[22] = 0;
            b[23] = 0;
            
            //4 bytes hold fixed destination addr. Converts the IPv4 address to Ipv6
            //bytes 24 to 39
            //bytes 24 to 33 set to 0;
            b[34] = (byte)255;
            b[35] = (byte)255;
            b[36] = addr[0];
            b[37] = addr[1];
            b[38] = addr[2];
            b[39] = addr[3];
           
            //Will hold response from server
            byte[] response = new byte[4];

            //Loop finds the payload length for each different sized packet and sends the complete
            //packet to the server
            for(int i = 2; i <=4096 ; i = i*2){
              //update data
              nBytes = 40 + i;

              //update length of payload          
              b[4] = (byte) ((i >> 8) & 0xFF);
              b[5] = (byte) (i & 0xFF);

             
              //Send bytes of current packet to server
              for(int v = 0; v<nBytes; v++)
              out.write(b[v]);

              //receive servers reply and print it to the screen
              System.out.println("data length: " + i);
              System.out.print("Response: 0x");
              is.read(response);
              for(int r = 0; r < response.length; r++){
                String hex = Integer.toHexString( (int) (response[r]) & 0XFF).toUpperCase();
                if(hex.length() != 2)
                System.out.print("0" + hex);
                else
                System.out.print(hex);
              }

              System.out.println("\n");
              
            }            

            System.out.println("Disconnected from server.");
      }
    }
  
}















