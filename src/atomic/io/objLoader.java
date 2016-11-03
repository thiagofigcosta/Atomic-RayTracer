/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atomic.io;

import atomic.vertices;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * @author nanotech
 */
public class objLoader {
    static final boolean closeFileAfterNoVertice=false;
    static final long LIMIT = 0;
    public static vertices load(String filename){
        vertices out=new vertices();
        boolean foundV=false;
        InputStream fis=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        try {
            fis = new FileInputStream("res/objs/"+filename);
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            long currentV=0;
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                if("v".equals(tokens[0])){//vertice
                    foundV=true;
                    out.add(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
                }else if(foundV&&closeFileAfterNoVertice)
                    break; 
                currentV++;
                if(LIMIT>0&&currentV>=LIMIT)
                    break;
            }
        } catch (IOException e) { 
            System.out.println("nao conseguimos achar o obj: "+filename+"  :(");
        } finally {
            if (br != null) { try { br.close(); } catch(Throwable t) {}}
            if (isr != null) { try { isr.close(); } catch(Throwable t) {}}
            if (fis != null) { try { fis.close(); } catch(Throwable t) {}}
        }
        return out;
    }
}
