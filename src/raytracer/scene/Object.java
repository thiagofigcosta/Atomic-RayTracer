package raytracer.scene;

import raytracer.Ray;
import raytracer.RayResponse;
import raytracer.math.Constants;
import raytracer.math.Vector3;

public class Object {

    public Pygment pygment;
    public Material material;
    public String type;
    public Vector3 position;
    public double radius;

    /**
     * *
     * Retorna um objeto do tipo RayResponse com informações sobre uma possível
     * interseção do raio com este objeto (veja o arquivo ray.h com a declaração
     * da struct RayResponse).
     *
     * O objeto response deve preencher os seguintes valores:
     *  - response.intersected, true/false indicando se houve interseção ou não
     *  - response.intersectionT, o valor T do raio (semi-reta) da primeira 
     *    interseção, caso haja mais que 1 interseção
     *  - response.intersectionPoint, contendo o ponto (x,y,z) de interseção
     *  - ray.intersectionNormal, contendo o vetor normal do objeto no
     *    ponto de interseção. A normal deve ser *normalizada* (norma = 1)
     * 
     *
     * @param ray
     * @return
     */
    public RayResponse intersectsWith(Ray ray) {
        RayResponse response = new RayResponse();
        Vector3 normalizedRay=ray.v.normalized();
        Vector3 meuP=position.diff(ray.p0);
        double meuA=1;
        double meuB=-2*meuP.dotProduct(normalizedRay);
        double meuC=meuP.dotProduct(meuP)-radius*radius;
        double meuPrimeiroT,meuSegundoT;
        double meuDelta=(meuB*meuB)-(4*meuA*meuC);
        
        if(meuDelta>=0){
            if(meuB<=0){
                meuPrimeiroT=(-meuB+Math.sqrt(meuDelta))/(meuA*2);
                if(meuPrimeiroT>0&&meuDelta!=0)
                    meuSegundoT=meuC/(meuA*meuPrimeiroT);
                else
                    meuSegundoT=meuPrimeiroT;
            }else{
                meuSegundoT=(-meuB-Math.sqrt(meuDelta))/(meuA*2);
                if(meuSegundoT>0&&meuDelta!=0)
                    meuPrimeiroT=meuC/(meuA*meuSegundoT);
                else
                    meuPrimeiroT=meuSegundoT;
            }
            response.intersected=true;
            if(meuPrimeiroT<=meuSegundoT)
                response.intersectionT=meuPrimeiroT;
            else
                response.intersectionT=meuSegundoT;
            response.intersectionPoint=ray.p0.add(normalizedRay.mult(response.intersectionT));
            response.intersectionNormal=response.intersectionPoint.diff(position).normalized();
        }else{
           response.intersected=false; 
           response.intersectionT=0;
           response.intersectionPoint=Vector3.ZERO;
           response.intersectionNormal=Vector3.ZERO;
        }
        return response;
    }

}
