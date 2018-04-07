package c2g2.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

    private int vaoId;

    private List<Integer> vboIdList;

    private int vertexCount;

    private Material material;
    
    private float[] pos;
    private float[] textco;
    private float[] norms;
    private int[] inds;
    
    
    public Mesh(){
       this(new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f,0.5f,0.0f,0.0f,0.5f,0.5f,0.5f,0.0f,0.0f,0.5f,0.0f,0.5f,0.5f,0.5f,0.0f,0.5f,0.5f,0.5f}, 
    			new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    			new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    			new int[]{0,6,4,0,2,6,0,3,2,0,1,3,2,7,6,2,3,7,4,6,7,4,7,5,0,4,5,0,5,1,1,5,7,1,7,3});
    }
    
    public void setMesh(float[] positions, float[] textCoords, float[] normals, int[] indices){
    	pos = positions;
    	textco = textCoords;
    	norms = normals;
    	inds = indices;
    	FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        //System.out.println("create mesh:");
        
        
        //System.out.println("v: "+positions.length+" t: "+textCoords.length+" n: "+normals.length+" idx: "+indices.length);
        try {
            vertexCount = indices.length;
            vboIdList = new ArrayList<Integer>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    	setMesh(positions, textCoords, normals, indices);        
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render() {
    		// Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
    
    public void scaleMesh(float sx, float sy, float sz){
    	cleanUp(); //clean up buffer
    	//Reset position of each point
    	//Do not change textco, norms, inds
    	//student code 
    	for (int i = 0; i < pos.length/3; i++) {
    		pos[i*3]=pos[i*3]*sx;          //simply change the x-y-z positions by ratio
    		pos[i*3+1]=pos[i*3+1]*sy;
    		pos[i*3+2]=pos[i*3+2]*sz;
		}   	
    	setMesh(pos, textco, norms, inds);
    }
    
    public void randMesh(){
    	cleanUp(); //clean up buffer
    	//Reset position of each point
    	//Do not change textco, norms, inds
    	//student code 
    	for (int i = 0; i < pos.length/3; i++) {
    		float tmp=(float) (0.92+ Math.random()*0.16);
    		pos[i*3]=pos[i*3]*tmp;          //make the sphere's surface uneven
    		pos[i*3+1]=pos[i*3+1]*tmp;
    		pos[i*3+2]=pos[i*3+2]*tmp;
		}   	
    	setMesh(pos, textco, norms, inds);
    }
    
    public void translateMesh(Vector3f trans){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	float x=trans.x;
    	float y=trans.y;
    	float z=trans.z;
    	for(int i=0; i< pos.length/3; i++){
    		pos[i*3]=pos[i*3]+x;           //simply change the x-y-z positions by adding constant
    		pos[i*3+1]=pos[i*3+1]+y;
    		pos[i*3+2]=pos[i*3+2]+z;
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    public void rotateMesh(Vector3f axis, float angle){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	
    	//this part references online resources about rotation about arbitrary axis
    	Vector3f u=axis.normalize();
    	double q0=Math.cos(Math.toRadians(angle/2));
    	q0=(float) q0;
    	double q1=Math.sin(Math.toRadians(angle/2))*u.x;
    	q1=(float) q1;
    	double q2=Math.sin(Math.toRadians(angle/2))*u.y;
    	q2=(float) q2;
    	double q3=Math.sin(Math.toRadians(angle/2))*u.z;
    	q3=(float) q3;
    	//when coding this part I did not use Matrix3f
    	float[][] mat= {
    			{(float) (q0*q0+q1*q1-q2*q2-q3*q3), (float) (2*(q1*q2-q0*q3)), (float) (2*(q1*q3+q0*q2))},
    			{(float) (2*(q1*q2+q0*q3)), (float) (q0*q0-q1*q1+q2*q2-q3*q3), (float) (2*(q2*q3-q0*q1))},
    			{(float) (2*(q1*q3-q0*q2)), (float) (2*(q3*q2+q0*q1)), (float) (q0*q0-q1*q1-q2*q2+q3*q3)}
    	};
    	for(int i=0; i< pos.length/3; i++){
    		float x1=pos[i*3]*mat[0][0]+pos[i*3+1]*mat[0][1]+pos[i*3+2]*mat[0][2];
    		float y1=pos[i*3+1]=pos[i*3]*mat[1][0]+pos[i*3+1]*mat[1][1]+pos[i*3+2]*mat[1][2];
    		float z1=pos[i*3+2]=pos[i*3]*mat[2][0]+pos[i*3+1]*mat[2][1]+pos[i*3+2]*mat[2][2];
    		//we should not update the positions too early thus we need to cache them
    		pos[i*3]=x1;
    		pos[i*3+1]=y1;
    		pos[i*3+2]=z1;
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    public void reflectMesh(Vector3f p, Vector3f n){
    	cleanUp();
    	//reset position of each point
    	//Do not change textco, norms, inds
    	//student code
    	
    	//the proof of this method of reflection will be placed in the report
    	float xr;float yr;float zr;float t;
    	float xp=p.x;float yp=p.y;float zp=p.z;
    	float xn=n.x;float yn=n.y;float zn=n.z;
    	for(int i=0; i< pos.length/3; i++){
    		xr=pos[i*3];
    		yr=pos[i*3+1];
    		zr=pos[i*3+2];
    		t=(xp*xn+yp*yn+zp*zn-xr*xn-yr*yn-zr*zn)/(xn*xn+yn*yn+zn*zn);
    		pos[i*3]=xr+2*t*xn;
    		pos[i*3+1]=yr+2*t*yn;
    		pos[i*3+2]=zr+2*t*zn;
    	}
    	setMesh(pos, textco, norms, inds);
    }
}
