package raytracer;

import raytracer.math.Vector3;
import raytracer.scene.Camera;
import raytracer.scene.Light;
import raytracer.scene.Material;
import raytracer.scene.Object;
import raytracer.scene.Pygment;
import raytracer.scene.Scene;

public class Raytracer {

    /**
     * *
     * Cria um raio que vai da posição da câmera e passa pelo pixel indicado por
     * (column, row).
     *
     * @param camera A câmera com as configurações de eye, target e up.
     * @param row A coordenada y do pixel por onde o raio deve passar.
     * @param column A coordenada x do pixel por onde o raio deve passar.
     * @param height O número de linhas totais da imagem.
     * @param width O número de colunas totais da image.
     * @return Um raio que sai da origem da câmera e passa pelo pixel (column,
     * row).
     */
    private Ray generateInitialRay(Camera camera, int row, int column,
            int height, int width) {
        Vector3 gridPointInCameraSpace = new Vector3(
                column - width / 2.0f,
                row - height / 2.0f,
                -1
        );

        Ray ray = new Ray();
        ray.p0 = camera.eye;
        ray.v = new Vector3(gridPointInCameraSpace.diff(ray.p0));
        ray.v.normalize();

        return ray;
    }
    
    /**
     * *
     * Lança um raio para a cena (camera) que passa por um certo pixel da cena.
     * Retorna a cor desse pixel.
     *
     * @param scene A cena onde o raio será lançado.
     * @param ray O raio a ser lançado.
     * @return A cor com que o pixel (acertado pelo raio) deve ser colorido.
     */
    private static final boolean lights=true;
    private Vector3 castRay(Scene scene, Ray ray, int amountOfRays, int maxInteractions) {
        Vector3 color=Vector3.ZERO;
        final double maximumBlur=0.006;
        RayResponse closestIntersection = new RayResponse();
        Object closestObjectHit = null;
        for (Object obj : scene.objects) {
            RayResponse response = obj.intersectsWith(ray);
            if (response.intersected) {
                if (response.intersectionT < closestIntersection.intersectionT) {
                    closestIntersection = response;
                    closestObjectHit = obj;
                }
            }
        }
        if (closestObjectHit != null) {
            Material material = closestObjectHit.material;
            Pygment pygment = closestObjectHit.pygment;
            color=color.add(pygment.color.mult(material.ambientCoefficient));
            for (Light light : scene.lights) {
                Ray lr=new Ray();
                lr.p0=closestIntersection.intersectionPoint;
                lr.v=light.position.diff(lr.p0).normalized();
                boolean hitsAnotherObjectBeforeLight = false;
                for (Object obj : scene.objects) {
                        RayResponse response = obj.intersectsWith(lr);
                        if (response.intersected) {
                            hitsAnotherObjectBeforeLight=true;
                        }
                }
                if (!hitsAnotherObjectBeforeLight) {
                    double dist=light.position.diff(lr.p0).norm();
                    Vector3 obs=scene.camera.eye.diff(lr.p0).normalized();
                    Vector3 luzComIntensidade=light.color.mult(1.0/(light.constantAttenuation+(light.linearAttenuation*dist)+(light.quadraticAttenuation*dist*dist)));
                    Vector3 difuse=pygment.color.mult(material.diffuseCoefficient).mult(Math.max(0, closestIntersection.intersectionNormal.dotProduct(lr.v)));
                    Vector3 espec=light.color.mult(material.specularCoefficient).mult(Math.pow(Math.max(0, lr.v.reflect(closestIntersection.intersectionNormal).dotProduct(obs)),material.specularExponent));
                    Vector3 allColors=difuse.add(espec);
                    color=color.add(luzComIntensidade.cwMult(allColors));
                }
            }
            if(!lights)color=pygment.color;
            
            if(maxInteractions>1){
                if(closestObjectHit.material.reflexivity!=0){
                    Ray nRay=new Ray();
                    nRay.p0=closestIntersection.intersectionPoint;
                    nRay.v=ray.v.reflect(closestIntersection.intersectionNormal);
                    if(amountOfRays!=-1)
                        color=color.mult(1-closestObjectHit.material.reflexivity).add(castRay(scene,nRay,-1,maxInteractions-1).mult(0.5));
                    else
                        color=color.add(castRay(scene,nRay,-1,maxInteractions-1).mult(0.5));
                }
                
                if(closestObjectHit.material.refractrability!=0){
                    Ray nRay=new Ray();
                    nRay.p0=closestIntersection.intersectionPoint;
                    final double airRefraction=1.000277;
                    double angleRatio=airRefraction/closestObjectHit.material.refractIndice;

                    boolean checkIfIsInSphere=true;//can slow process

                    if(checkIfIsInSphere){
                     //TODO: verificar se ponto (t*0.99) está dentro de alguma esfera, se estiver angleRatio=1/angleRatio;   
                    }

                    double cosInc=ray.v.dotProduct(closestIntersection.intersectionNormal);//Math.acos(ray.v.dotProduct(closestIntersection.intersectionNormal)/(ray.v.norm()*closestIntersection.intersectionNormal.norm()));
                    double cosRef=Math.sqrt(1-Math.pow(angleRatio, 2)*(1-Math.pow(cosInc, 2)));
                    nRay.v=closestIntersection.intersectionNormal.mult((angleRatio*cosInc-cosRef)).diff(ray.v.mult(angleRatio));
                    if(amountOfRays!=-1)
                        color=color.mult(1-closestObjectHit.material.refractrability).add(castRay(scene,nRay,-1,maxInteractions-1));
                    else
                        color=color.add(castRay(scene,nRay,-1,maxInteractions-1));
                }
            }
            
            if(amountOfRays>1){
                int sqrtnOfRays=(int) Math.floor(Math.sqrt(amountOfRays)); 
                double resolution=(double)maximumBlur/sqrtnOfRays;
                Ray blurRay= new Ray();
                blurRay.p0=ray.p0;
                blurRay.v=ray.v;
                blurRay.v.x-=maximumBlur/2;
                int nOfRays=0;
                double backupy=blurRay.v.y-maximumBlur/2;
                for(int i=0;i<sqrtnOfRays;i++){
                    blurRay.v.y=backupy;
                    for(int j=0;j<sqrtnOfRays;j++){
                        Vector3 rrc=castRay(scene, blurRay, 1, -1);
                        color=color.add(rrc);
                        blurRay.v.y+=resolution;
                        nOfRays++;
                    }
                    blurRay.v.x+=resolution;
                }
                color=color.mult(1.0/nOfRays);
                color.truncate();
            }
        }
        return color;
        /*
        if (closestObjectHit != null){//reflexao e refracao
            if(closestObjectHit.material.reflexivity!=0&&maxRays>0){
                Ray nRay=new Ray();
                nRay.p0=closestIntersection.intersectionPoint;
                nRay.v=ray.v.refract(closestIntersection.intersectionNormal);
                if(amountOfRays!=-1)
                    color=color.mult(1-closestObjectHit.material.reflexivity).add(castRay(scene,nRay,-1,maxRays-1).mult(1/2));
                else
                    color=color.add(castRay(scene,nRay,-1,maxRays-1).mult(1/2));

            }
            
            
            if(closestObjectHit.material.refractrability!=0&&maxRays>0){
                Ray nRay=new Ray();
                nRay.p0=closestIntersection.intersectionPoint;
                final double airRefraction=1.000277;
                double angleRatio=airRefraction/closestObjectHit.material.refractIndice;

                boolean checkIfIsInSphere=true;//can slow process

                if(checkIfIsInSphere){
                 //TODO: verificar se ponto (t*0.99) está dentro de alguma esfera, se estiver angleRatio=1/angleRatio;   
                }

                double cosInc=ray.v.dotProduct(closestIntersection.intersectionNormal);//Math.acos(ray.v.dotProduct(closestIntersection.intersectionNormal)/(ray.v.norm()*closestIntersection.intersectionNormal.norm()));
                double cosRef=Math.sqrt(1-Math.pow(angleRatio, 2)*(1-Math.pow(cosInc, 2)));
                nRay.v=closestIntersection.intersectionNormal.mult((angleRatio*cosInc-cosRef)).diff(ray.v.mult(angleRatio));
                if(amountOfRays!=-1)
                    color=color.mult(1-closestObjectHit.material.refractrability).add(castRay(scene,nRay,-1,maxRays-1));
                else
                    color=color.add(castRay(scene,nRay,-1,maxRays-1));
            }
        }*/
    }

    /**
     * *
     * MÉTODO que renderiza uma cena, gerando uma matriz de cores.
     *
     * @param scene um objeto do tipo Scene contendo a descrição da cena (ver
     * Scene.java)
     * @param height altura da imagem que estamos gerando (e.g., 600px)
     * @param width largura da imagem que estamos gerando (e.g., 800px)
     * @return uma matriz de cores (representadas em Vector3 - r,g,b)
     */
    public Vector3[][] renderScene(Scene scene, int height, int width) {
        Vector3[][] pixels = new Vector3[height][width];

        // Para cada pixel, lança um raio
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // cria um raio primário
                Ray ray = generateInitialRay(scene.camera, i, j, height, width);

                // lança o raio e recebe a cor
                Vector3 color = castRay(scene, ray,64,10);

                // salva a cor na matriz de cores
                pixels[i][j] = color;
            }
        }

        return pixels;
    }
}
