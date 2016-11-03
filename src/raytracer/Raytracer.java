package raytracer;

import raytracer.math.Vector3;
import raytracer.scene.Camera;
import raytracer.scene.Object;
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
    private Vector3 castRay(Scene scene, Ray ray, int amountOfRays) {
        // Para todos os objetos da cena, verifica se o raio o acerta e pega aquele
        // que foi atingido primeiro (menor "t")
        Vector3 color=null;
        
        final double maximumBlur=0.005;
        
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
            color=closestObjectHit.pygment.color;
	}else
	     color = new Vector3(0, 0, 0);
        
        if(amountOfRays>1){
            int sqrtnOfRays=(int) Math.floor(Math.sqrt(amountOfRays));
            double resolution=maximumBlur/sqrtnOfRays;
            ray.v.x-=maximumBlur/2;
            double backupy=ray.v.y;
            int nOfColors=1;
            for(int i=0;i<sqrtnOfRays;i++){
                ray.v.y=backupy-maximumBlur/2;
                for(int j=0;j<sqrtnOfRays;j++){
                    //raio distorcido
                    closestIntersection = new RayResponse();
                    closestObjectHit = null;
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
                        color=color.add(closestObjectHit.pygment.color);
                        nOfColors++;
                    }else{
                        nOfColors++;
                    }
                    //raio distocido
                    ray.v.y+=resolution;
                }
                ray.v.x+=resolution;
            }
            color=color.mult(1.0/nOfColors);
        }
        
        return color;
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
                Vector3 color = castRay(scene, ray,64);

                // salva a cor na matriz de cores
                pixels[i][j] = color;
            }
        }

        return pixels;
    }
}
