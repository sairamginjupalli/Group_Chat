import java.io.*;                                                                                             
import java.net.Socket;
public class EchoClient {
    public static void main(String[] args){
        try{
            System.out.println("Client Started");
            Socket soc=new Socket("localhost",9806);
            BufferedReader userInput=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter the String");
            String str=userInput.readLine();
            PrintWriter out =new PrintWriter(soc.getOutputStream(),true);
            out.println(str);
            BufferedReader in=new BufferedReader(new InputStreamReader(soc.getInputStream()));
            System.out.println(in.readLine());
            soc.close();


        }
        catch(Exception e){
           e.printStackTrace();
        }
    }
}
