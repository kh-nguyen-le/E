import java.net.*;
import java.io.*;
 
public class motorServer {
    public static void main(){

        try ( 
            ServerSocket mServer = new ServerSocket(4444);
            Socket mClient = mServer.accept();
            PrintWriter out = new PrintWriter(mClient.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
			) {
         
            String inputLine, outputLine;
             
			
            if ((inputLine = in.readLine()) == "Connected\n") {
				//test motor thing
                outputLine = "right\n";
                out.println(outputLine);
				sleep(10); //let motor finish turning before testing turning left
				outputLine = "left\n";
                out.println(outputLine);
              
            }
        } 
    }
}
