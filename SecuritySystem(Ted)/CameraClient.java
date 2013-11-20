
package RPiSecurityServer;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.net.*;

public class CameraClient {

    Socket toServer;
    BufferedImage image;
    URL url;
    HttpURLConnection con;
    int port;
    boolean motionDetected = false;

    public CameraClient() throws MalformedURLException, IOException{

        port = 8080;
        url = new URL("http:8080//anearcan/");

    }
 /*
    public void runHTTPClient() throws IOException{
        if (url.getPort() != -1) port = url.getPort();
         if (!(url.getProtocol().equalsIgnoreCase("http"))) {
           System.err.println("Sorry. I only understand http.");
         }
         toServer = new Socket(url.getHost(), port);
         OutputStream theOutput = toServer.getOutputStream();
         // no auto-flushing
         PrintWriter pw = new PrintWriter(theOutput, false);
         // native line endings are uncertain so add them manually
         pw.print("POST " + url.getFile() + " HTTP/1.0\r\n");
         pw.print("Accept: text/plain, text/html, text/*\r\n");
         pw.print("\r\n");
         pw.flush();
         InputStream in = toServer.getInputStream();
         InputStreamReader isr = new InputStreamReader(in);
         BufferedReader br = new BufferedReader(isr);
         int c;
         while ((c = br.read()) != -1) {
           System.out.print((char) c);
         }
    }
*/

    public void runSocketClient() throws UnknownHostException, IOException {

        String testMsg;

        toServer = new Socket("192.168.0.11", port);
        OutputStream out = toServer.getOutputStream();
        PrintWriter pw = new PrintWriter(out, true);
        InputStream in = toServer.getInputStream();
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        //DataInputStream din = new DataInputStream(toServer.getInputStream());
        //DataOutputStream dout = new DataOutputStream(toServer.getOutputStream());

        while(true){
            while((testMsg = br.readLine())!= null){
                System.out.println(testMsg);
                if(testMsg.equalsIgnoreCase("connection successful")){
                    System.out.println("Connection to anearcan successful");
                    pw.println("Camera: Received ACK");
                }
                if(testMsg.equalsIgnoreCase("Snapshot rcvd")){
                    System.out.println("Reset Motion sensor");
                }
                if(motionDetected || testMsg.equalsIgnoreCase("Snapshot Request")){
                    System.out.println("Send image taken to server");
		   //Taking a picture with the  PI_CAMERA with the use of 'raspistill' command line command
 		    Runtime rt = Runtime.getRuntime();
		    Process pr = rt.exec("raspistill -v -o snapshot1.png");
		   //Picture stored written on socket
                    image = ImageIO.read(new File("snapshot1.png"));
                    ImageIO.write(image,"PNG",toServer.getOutputStream());
                }
            }
       }
   }

    public static void main(String[] args) throws MalformedURLException, IOException{
        CameraClient client  = new CameraClient();
        client.runSocketClient();
    }
}

