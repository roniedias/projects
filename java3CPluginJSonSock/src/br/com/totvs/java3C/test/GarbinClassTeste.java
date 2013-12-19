package br.com.totvs.java3C.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;



public class GarbinClassTeste {
             
       private Socket s;
       private PrintStream ps;
       private BufferedReader in;

       private String out;

       private String str;
             
             
       public GarbinClassTeste(String ip, String porta, String ambiente) {
                    
             try {
                s = new Socket("172.16.102.37", 9130);
                ps = new PrintStream(s.getOutputStream());  
                in = new BufferedReader(new InputStreamReader(s.getInputStream())); 
                str = "LATENCY#4$10.10.3.38#10301#KPMG_PRODUCAO#04/12/2013#13:35:00$10.10.3.38#10302#KPMG_PRODUCAO#04/12/2013#13:35:00$10.10.3.38#10303#KPMG_PRODUCAO#04/12/2013#13:35:00$10.10.3.38#10304#KPMG_PRODUCAO#04/12/2013#13:35:00$#";


                ps.print(str + '\0');

                out = in.readLine();
                System.out.print(out);

                s.close();
             }
        catch (IOException e) {
             System.out.println("Nao foi possivel conectar-se ao servico TCP!");
             System.out.println(e.getMessage());
             System.exit(3);
        }
                    
       }
       
              
       
       public static void main(String[] args) {
            new GarbinClassTeste("10.10.3.38", "10317", "KPMG_PRODUCAO");
       }
             

}
