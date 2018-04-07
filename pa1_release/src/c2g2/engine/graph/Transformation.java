package c2g2.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import c2g2.engine.GameItem;

public class Transformation {

    private final Matrix4f projectionMatrix;
    
    private final Matrix4f viewMatrix;
    
    private final Matrix4f modelMatrix;


    public Transformation() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        projectionMatrix.identity();
        //// --- student code ---
        
        //basic projection matrix with FOV
        //f=2*n/(t-b)
        
        
        float r=width/height;
        double f=1/(Math.tan(fov/2));
        //by default l+r=0, b+t=0
        projectionMatrix.set((float) f/r,0,0,0,
	             		      0,(float) f,0,0,
	             		      0,0,(zFar+zNear)/(zNear-zFar),2*zFar*zNear/(zNear-zFar),
	             		      0,0,-1,0);
        //the set() function set column elements first but I would like to make it more like general matrix,
        //thus another transpose operation should be added.
        projectionMatrix.transpose();

        return projectionMatrix;
    }
    
    public Matrix4f getViewMatrix(Camera camera) {

        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
            .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
                
    }
    
    public Matrix4f getModelMatrix(GameItem gameItem){
        Vector3f rotation = gameItem.getRotation();
        Vector3f position = gameItem.getPosition();
        modelMatrix.identity();
        //// --- student code ---
        float sinx = (float) Math.sin(Math.toRadians(rotation.x));
        float cosx = (float) Math.cos(Math.toRadians(rotation.x));
        float siny = (float) Math.sin(Math.toRadians(rotation.y));
        float cosy = (float) Math.cos(Math.toRadians(rotation.y));
        float sinz = (float) Math.sin(Math.toRadians(rotation.z));
        float cosz = (float) Math.cos(Math.toRadians(rotation.z));
        //based on the rotation vector we decompose the rotation part into 3 rotation along x,y,z axis
        Matrix4f T=new Matrix4f(1,0,0,position.x,
                0,1,0,position.y,
                0,0,1,position.z,
                0,0,0,1);
        T.transpose();
        modelMatrix.mul(T);
        Matrix4f R1=new Matrix4f(1,0,0,0,
        		                 0,cosx,-sinx,0,
        		                 0,sinx,cosx,0,
        		                 0,0,0,1);
        R1.transpose();
        Matrix4f R2=new Matrix4f(cosy,0,siny,0,
                				 0,1,0,0,
                				 -siny,0,cosy,0,
                				 0,0,0,1);
        R2.transpose();
        Matrix4f R3=new Matrix4f(cosz,-sinz,0,0,
				 				 sinz,cosz,0,0,
				 				 0,0,1,0,
				 				 0,0,0,1);
        R3.transpose();
        modelMatrix.mul(R1);modelMatrix.mul(R2);modelMatrix.mul(R3);
        //based on the position vector we can construct the translation matrix
        modelMatrix.scale(gameItem.getScale());
//combine those matrix together
        return modelMatrix;
    	
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {

    	Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(getModelMatrix(gameItem));

    }

}
