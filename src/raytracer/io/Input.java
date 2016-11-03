package raytracer.io;

import atomic.Util;
import atomic.io.objLoader;
import atomic.vertices;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import raytracer.math.Vector3;
import raytracer.scene.Light;
import raytracer.scene.Material;
import raytracer.scene.Object;
import raytracer.scene.Pygment;
import raytracer.scene.Scene;

public class Input {

    public static Scene readSceneFromFile(String fileName) throws IOException {
        System.out.println("Carregando arquivo: " + fileName);
        Scene scene = new Scene();
        Locale.setDefault(Locale.ENGLISH);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("res/scenes/"+fileName)));
        Scanner scanner;
        //-----------------------------------------------------------------
        // lendo a CÂMERA
        scanner = new Scanner(br.readLine());
        scene.camera.eye = new Vector3(
                scanner.nextDouble(),
                scanner.nextDouble(),
                scanner.nextDouble()
        );
        scanner = new Scanner(br.readLine());
        scene.camera.target = new Vector3(
                scanner.nextDouble(),
                scanner.nextDouble(),
                scanner.nextDouble()
        );
        scanner = new Scanner(br.readLine());
        scene.camera.up = new Vector3(
                scanner.nextDouble(),
                scanner.nextDouble(),
                scanner.nextDouble()
        );

        //-----------------------------------------------------------------
        // lendo as FONTES DE LUZ
        scanner = new Scanner(br.readLine());
        int numOfLights = scanner.nextInt();
        scene.lights = new ArrayList<Light>(numOfLights);
        while (numOfLights > 0) {
            scanner = new Scanner(br.readLine());
            Light light = new Light();
            light.position = new Vector3(
                    scanner.nextDouble(),
                    scanner.nextDouble(),
                    scanner.nextDouble()
            );
            light.color = new Vector3(
                    scanner.nextDouble(),
                    scanner.nextDouble(),
                    scanner.nextDouble()
            );
            light.constantAttenuation = scanner.nextDouble();
            light.linearAttenuation = scanner.nextDouble();
            light.quadraticAttenuation = scanner.nextDouble();

            scene.lights.add(light);
            numOfLights--;
        }

        //-----------------------------------------------------------------
        // lendo os PIGMENTOS
        scanner = new Scanner(br.readLine());
        int numOfPygments = scanner.nextInt();
        scene.pygments = new ArrayList<Pygment>(numOfPygments);
        while (numOfPygments > 0) {
            scanner = new Scanner(br.readLine());
            Pygment pygment = new Pygment();
            pygment.type = scanner.next();
            pygment.color = new Vector3(
                    scanner.nextDouble(),
                    scanner.nextDouble(),
                    scanner.nextDouble()
            );

            scene.pygments.add(pygment);
            numOfPygments--;
        }

        //-----------------------------------------------------------------
        // lendo os MATERIAIS
        scanner = new Scanner(br.readLine());
        int numOfMaterials = scanner.nextInt();
        scene.materials = new ArrayList<Material>(numOfMaterials);
        while (numOfMaterials > 0) {
            scanner = new Scanner(br.readLine());
            Material material = new Material();
            material.ambientCoefficient = scanner.nextDouble();
            material.diffuseCoefficient = scanner.nextDouble();
            material.specularCoefficient = scanner.nextDouble();
            material.specularExponent = scanner.nextDouble();
            material.reflectionCoefficient = scanner.nextDouble();
            material.transmissionCoefficient = scanner.nextDouble();
            material.snellCoefficient = scanner.nextDouble();
            
            try{ material.refractrability=scanner.nextDouble();}catch(Exception e){}
            try{ material.refractIndice=scanner.nextDouble();}catch(Exception e){}
            try{ material.reflexivity=scanner.nextDouble();}catch(Exception e){}

            scene.materials.add(material);
            numOfMaterials--;
        }

        //-----------------------------------------------------------------
        // lendo os OBJETOS DA CENA
        scanner = new Scanner(br.readLine());
        int numOfObjects = scanner.nextInt();
        scene.objects = new ArrayList<Object>(numOfObjects);
        while (numOfObjects > 0) {
            scanner = new Scanner(br.readLine());
            Object object = new Object();
            object.pygment = scene.pygments.get(scanner.nextInt());
            object.material = scene.materials.get(scanner.nextInt());
            object.type = scanner.next();
            object.position = new Vector3(
                    scanner.nextDouble(),
                    scanner.nextDouble(),
                    scanner.nextDouble()
            );
            object.radius = scanner.nextDouble();

            scene.objects.add(object);
            numOfObjects--;
        }
        //-----------------------------------------------------------------
        //lendo os OBJS
        //.txt: n of Objs 
        //.txt: filename pigmento material descricao position raio rotateX rotateY rotateZ scaleX scaleY scaleZ
        try{
            scanner = new Scanner(br.readLine());
            int numOfObjs = scanner.nextInt();
            while (numOfObjs > 0) {
                scanner = new Scanner(br.readLine());
                String filename=scanner.next();
                Pygment objPiyg=scene.pygments.get(scanner.nextInt());
                Material objMat=scene.materials.get(scanner.nextInt());
                String aboutObj=scanner.next();
                Vector3 objPos = new Vector3(
                        scanner.nextDouble(),
                        scanner.nextDouble(),
                        scanner.nextDouble()
                );
                double objRadius = scanner.nextDouble();
                double rotX=scanner.nextDouble();
                double rotY=scanner.nextDouble();
                double rotZ=scanner.nextDouble();
                double sclX=scanner.nextDouble();
                double sclY=scanner.nextDouble();
                double sclZ=scanner.nextDouble();
                vertices obj=objLoader.load(filename);
                if(rotX!=0||rotY!=0||rotZ!=0){
                    double[][] rotate=Util.rotateCoords(rotX, rotY, rotZ);
                    for(Vector3 v:obj.getVrtcs())
                        v=Util.rotatePoint(v, rotate);
                }
                if(sclX!=1||sclY!=1||sclZ!=1)
                    for(Vector3 v:obj.getVrtcs())
                        v=Util.scalePoint(v, sclX,sclY,sclZ);
                for(Vector3 v:obj.getVrtcs()){
                    Object objWave = new Object();
                    objWave.pygment=objPiyg;
                    objWave.material=objMat;
                    objWave.type=aboutObj;
                    objWave.position=v.add(objPos);
                    objWave.radius=objRadius;
                    scene.objects.add(objWave);
                }
                numOfObjs--;
            }
        }catch(Exception e){
            System.out.println("arquivo sem indicador de waveform obj");
        }
        br.close();
        scene.camera.calculateBase();
        System.out.println(scene.debugInfo());

        return scene;
    }
}
