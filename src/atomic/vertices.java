package atomic;

import java.util.ArrayList;
import raytracer.math.Vector3;

public class vertices {
    ArrayList<Vector3> vrtcs;

    public vertices() {
        vrtcs=new ArrayList<Vector3>();
    }
    
    public void add(double a,double b,double c){
        vrtcs.add(new Vector3(a,b,c));
    }
    
    
}