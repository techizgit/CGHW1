package c2g2.game;

import org.joml.Vector2f;
import org.joml.Vector3f;


import static org.lwjgl.glfw.GLFW.*;

import java.util.Random;

import c2g2.engine.GameItem;
import c2g2.engine.IGameLogic;
import c2g2.engine.MouseInput;
import c2g2.engine.Window;
import c2g2.engine.graph.Camera;
import c2g2.engine.graph.DirectionalLight;
import c2g2.engine.graph.Material;
import c2g2.engine.graph.Mesh;
import c2g2.engine.graph.OBJLoader;
import c2g2.engine.graph.PointLight;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    
    private static final float SCALE_STEP = 0.01f;
    
    private static final float TRANSLATE_STEP = 0.01f;
    
    private static final float ROTATION_STEP = 0.3f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private Vector3f ambientLight;

    private PointLight[] pointLightList;


    private DirectionalLight directionalLight;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;
    
    private int currentObj;
  //generating random parameters for planets
    float r2 = (float) (5 + Math.random() * 1); //orbit radius
    float r3 = (float) (8 + Math.random() * 2);
    float r4 = (float) (12 + Math.random() * 3);
    float r5 = (float) (17 + Math.random() * 4);

    
    float d2 = (float) (0.3 + Math.random() * 0.4);//planet radius
    float d3 = (float) (0.3 + Math.random() * 0.5);
    float d4 = (float) (0.3 + Math.random() * 0.6);
    float d5 = (float) (0.3 + Math.random() * 0.7);
    float dm = (float) (0.1 + Math.random() * (0.5 * d3 - 0.1));
    float rm = (float) (d3+0.5+ Math.random() * 0.5); //make sure planets do not "collide" with each other
    
    float w2 = (float) (Math.pow(r2, -1.5));//orbiting angular speed
    float w3 = (float) (Math.pow(r3, -1.5));
    float w4 = (float) (Math.pow(r4, -1.5));
    float w5 = (float) (Math.pow(r5, -1.5));
    float wm = 0.15f;
    
    float a2=(float) (Math.random() * 360);//orbit angle
    float a3=(float) (Math.random() * 360);
    float a4=(float) (Math.random() * 360);
    float a5=(float) (Math.random() * 360);
    float am=(float) (Math.random() * 360);
    
    float sw2 = (float) (10+Math.random() * 20);//self-rotation angular speed
    float sw3 = (float) (10+Math.random() * 20);
    float sw4 = (float) (10+Math.random() * 20);
    float sw5 = (float) (10+Math.random() * 20);
    float swm = (float) (10+Math.random() * 20);
    
    float sa2=(float) (Math.random() * 360);//self-rotation angle
    float sa3=(float) (Math.random() * 360);
    float sa4=(float) (Math.random() * 360);
    float sa5=(float) (Math.random() * 360);
    float sam=(float) (Math.random() * 360);
    
    float pause=1;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
        currentObj=0;
        
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float reflectance = 1f;        
        // NOTE: 
        //   please uncomment following lines to test your OBJ Loader.
        Mesh mesh = OBJLoader.loadMesh("src/resources/models/ball.obj");
        Mesh mesh2 = OBJLoader.loadMesh("src/resources/models/ball.obj");
        Mesh mesh3 = OBJLoader.loadMesh("src/resources/models/ball.obj");
        Mesh mesh4 = OBJLoader.loadMesh("src/resources/models/ball.obj");
        Mesh mesh5 = OBJLoader.loadMesh("src/resources/models/ball.obj");
        Mesh meshm = OBJLoader.loadMesh("src/resources/models/ball.obj");
        

        //Mesh mesh = new Mesh();  // comment this line when you enable OBJLoader
        Material material = new Material(new Vector3f(1.0f, 0.75f, 0.75f), reflectance);
        
        Material material2 = new Material(new Vector3f((float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5)), reflectance);
        Material material3 = new Material(new Vector3f((float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5)), reflectance);
        Material material4 = new Material(new Vector3f((float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5)), reflectance);
        Material material5 = new Material(new Vector3f((float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5)), reflectance);
        Material materialm = new Material(new Vector3f((float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5), (float) (0.5 + Math.random() * 0.5)), reflectance);
        
        mesh.setMaterial(material);//setting different colors
        mesh2.setMaterial(material2);
        mesh3.setMaterial(material3);
        mesh4.setMaterial(material4);
        mesh5.setMaterial(material5);
        meshm.setMaterial(materialm);
        
        mesh2.randMesh();//make the sphere's surface uneven
        mesh3.randMesh();
        mesh4.randMesh();
        mesh5.randMesh();
        meshm.randMesh();
        
        GameItem gameItem = new GameItem(mesh);
        GameItem gameItem2 = new GameItem(mesh2);
        GameItem gameItem3 = new GameItem(mesh3);
        GameItem gameItem4 = new GameItem(mesh4);
        GameItem gameItem5 = new GameItem(mesh5);
        GameItem gameItemm = new GameItem(meshm);
                     
        gameItem.setPosition(0, 0, 0);
        gameItem2.setPosition(0, 0,  r2);
        gameItem3.setPosition(0, 0,  r3);
        gameItem4.setPosition(0, 0,  r4);
        gameItem5.setPosition(0, 0,  r5);
        gameItemm.setPosition(0, 0,  rm);
        
        gameItem.setScale(2);
        gameItem2.setScale(d2);
        gameItem3.setScale(d3);
        gameItem4.setScale(d4);
        gameItem5.setScale(d5);
        gameItemm.setScale(dm);
        

        gameItems = new GameItem[]{gameItem,gameItem2,gameItem3,gameItem4,gameItem5,gameItemm};
        //gameItems = new GameItem[]{gameItem,gameItem2};

        ambientLight = new Vector3f(0.35f, 0.35f, 0.35f);
        Vector3f lightColour = new Vector3f(1, 0.9f, 0.8f);
        //Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 20.0f;
        
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
      //adding point lights at 6 positions
        PointLight pointLight1 = new PointLight(lightColour, new Vector3f(4.5f, 0, 0), lightIntensity);
        PointLight pointLight2 = new PointLight(lightColour, new Vector3f(0, 4.5f, 0), lightIntensity);
        PointLight pointLight3 = new PointLight(lightColour, new Vector3f(0, 0, 4.5f), lightIntensity);
        PointLight pointLight4 = new PointLight(lightColour, new Vector3f(-4.5f, 0, 0), lightIntensity);
        PointLight pointLight5 = new PointLight(lightColour, new Vector3f(0, -4.5f, 0), lightIntensity);
        PointLight pointLight6 = new PointLight(lightColour, new Vector3f(0, 0, -4.5f), lightIntensity);
        
        pointLight1.setAttenuation(att);
        pointLight2.setAttenuation(att);
        pointLight3.setAttenuation(att);
        pointLight4.setAttenuation(att);
        pointLight5.setAttenuation(att);
        pointLight6.setAttenuation(att);
        
        pointLightList = new PointLight[]{pointLight1,pointLight2,pointLight3,pointLight4,pointLight5,pointLight6};
        //now we can render a list of lights
        
        
/*        Mesh mesh = OBJLoader.loadMesh("src/resources/models/bunny.obj");
        Material material = new Material(new Vector3f(1.0f, 1.0f, 1.0f), reflectance);
        mesh.setMaterial(material);
        GameItem gameItem = new GameItem(mesh);
        gameItems = new GameItem[]{gameItem};
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 0.9f, 0.8f);
        //Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 10.0f;
        PointLight pointLight = new PointLight(lightColour, new Vector3f(-4.5f, 0, 0), lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        pointLightList = new PointLight[]{pointLight};*/
        

        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        lightColour = new Vector3f(0, 0, 0);
        directionalLight = new DirectionalLight(lightColour, lightPosition, 0.0f);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {


        cameraInc.set(0, 0, 0);
        pause=1;
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -3;
            //Press W to go forward
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 3;
          //Press S to go back
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -3;
          //Press A to go left
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 3;
          //Press D to go right
        }
         if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -3;
          //Press Z to go down
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 3;
          //Press X to go up
        }
        else if(window.isKeyPressed(GLFW_KEY_1)){
            //get screenshot
            renderer.writePNG(window);
        }
        if(window.isKeyPressed(GLFW_KEY_SPACE)){
            //hold P to pause
            pause=0;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        
        if (mouseInput.isRightButtonPressed()) {
        	
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            //rotate the camera based on user mouse input while holding right button

        }

        a2=a2+w2*pause;a3=a3+w3*pause;a4=a4+w4*pause;a5=a5+w5*pause;am=am+wm*pause;
        sa2=sa2+sw2*pause;sa3=sa3+sw3*pause;sa4=sa4+sw4*pause;sa5=sa5+sw5*pause;sam=sam+swm*pause;
        gameItems[1].setPosition((float) (r2*Math.sin(a2)), 0, (float) (r2*Math.cos(a2)));//setting planet position
        gameItems[1].setRotation(0,sa2,0);//setting planet rotation
        gameItems[2].setPosition((float) (r3*Math.sin(a3)), 0, (float) (r3*Math.cos(a3)));
        gameItems[2].setRotation(0,sa2,0);
        gameItems[3].setPosition((float) (r4*Math.sin(a4)), 0, (float) (r4*Math.cos(a4)));
        gameItems[3].setRotation(0,sa3,0);
        gameItems[4].setPosition((float) (r5*Math.sin(a5)), 0, (float) (r5*Math.cos(a5)));
        gameItems[4].setRotation(0,sa5,0);
        float x=gameItems[2].getPosition().x; float z=gameItems[2].getPosition().z;
        gameItems[5].setPosition((float) (x+rm*Math.sin(am)), 0, (float) (z+rm*Math.cos(am)));//setting moon position
        gameItems[5].setRotation(0,sam,0);
               
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems, ambientLight, pointLightList, directionalLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}
