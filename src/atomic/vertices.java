package atomic;

import java.util.ArrayList;
import raytracer.math.Vector3;

public class vertices {
    ArrayList<Vector3> vrtcs;

    public ArrayList<Vector3> getVrtcs() {
        return vrtcs;
    }

    public vertices() {
        vrtcs=new ArrayList<Vector3>();
    }
    
    public void add(double a,double b,double c){
        vrtcs.add(new Vector3(a,b,c));
    }

    @Override
    public String toString() {
        return "vertices{" + "vrtcs=" + vrtcs + '}';
    }
    
    
}