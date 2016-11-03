/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atomic.io;

import atomic.vertices;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nanotech
 */
public class objLoader {
   
    public static vertices load(String filename){
        vertices out=new vertices();
       
        try {
            InputStream fis = new FileInputStream("res/objs"+filename);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                if("v".equals(tokens[0])){//vertice
                    out.add(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
                }
            }
        } catch (IOException e) { 
            System.out.println("nao conseguimos achar o obj: "+filename+"  :(");
        }
        return out;
    }
}
