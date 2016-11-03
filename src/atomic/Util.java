package atomic;

import raytracer.math.Vector3;

public class Util {
    
    public static final double PI=3.1415926535897;
    
    public static double radToAngle(double angle){
        return angle*180/PI;
    }
    
    public static double angleToRad(double rad){
        return rad*PI/180;
    }
    
    public static double[][] multiplyMatrix(double[][] m1,double[][] m2){ 
        double[][] out = new double[m1.length][m2[0].length];
        if(m1[0].length!=m2.length) return out;//n se aplica
        for(int i=0;i<m1.length;i++)
            for(int j=0;j<m2[0].length;j++)
                 out[i][j]=0;
        for(int i=0;i<m1.length;i++)
            for(int j=0;j<m2[0].length;j++)
                for(int k=0;k<m1[0].length;k++)
                   out[i][j]+=m1[i][k]*m2[k][j];
    return out;
    }
    
    public static double[][] getIdentity(){
        double[][] out = new double[4][4];
        for(int i=0;i<out.length;i++)
            for(int j=0;j<out[0].length;j++)
                 out[i][j]=0;
        out[0][0]=1;
        out[1][1]=1;
        out[2][2]=1;
        out[3][3]=1;
        return out;
    }
    
    public static Vector3 rotatePoint(Vector3 point,double[][] matrix){
        double[][] pointM=new double[1][4];
        pointM[0][0]=point.x;
        pointM[0][1]=point.y;
        pointM[0][2]=point.z;
        pointM[0][3]=1;
        pointM=multiplyMatrix(pointM,matrix);
        point.x=pointM[0][0];
        point.y=pointM[0][1];
        point.z=pointM[0][2];
        return point;
    }
    
    public static double[][] rotateCoords(double angleX,double angleY,double angleZ){
        double[][] out = getIdentity();
        if(angleX!=0){
            double[][] second = getIdentity();
            out[1][1]=Math.cos(angleToRad(angleX));
            out[1][2]=-Math.sin(angleToRad(angleX));
            out[2][2]=Math.cos(angleToRad(angleX));
            out[2][1]=Math.sin(angleToRad(angleX));
            out=multiplyMatrix(out,second);
        }
        if(angleY!=0){
            double[][] second = getIdentity();
            out[0][0]=Math.cos(angleToRad(angleY));
            out[0][2]=Math.sin(angleToRad(angleY));
            out[2][2]=Math.cos(angleToRad(angleY));
            out[0][2]=-Math.sin(angleToRad(angleY));
            out=multiplyMatrix(out,second);
        }
        if(angleZ!=0){
            double[][] second = getIdentity();
            out[0][0]=Math.cos(angleToRad(angleZ));
            out[0][1]=-Math.sin(angleToRad(angleZ));
            out[1][1]=Math.cos(angleToRad(angleZ));
            out[1][0]=Math.sin(angleToRad(angleZ));
            out=multiplyMatrix(out,second);
        }
        return out;
    }
}
