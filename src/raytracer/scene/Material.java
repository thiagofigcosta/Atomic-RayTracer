package raytracer.scene;

public class Material {

    public double ambientCoefficient;
    public double diffuseCoefficient;
    public double specularCoefficient;
    public double specularExponent;

    public double reflectionCoefficient;
    public double transmissionCoefficient;
    public double snellCoefficient;
    
    public double refractrability=0;//0-1          1=transparente
    public double refractIndice=0;//indice de refracao
    public double reflexivity=0;//0-1              1=espelho 
}
