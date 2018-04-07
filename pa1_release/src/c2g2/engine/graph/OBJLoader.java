package c2g2.engine.graph;

import java.io.BufferedReader;
import java.util.Arrays;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.apache.commons.lang3.ArrayUtils;

public class OBJLoader {
    public static Mesh loadMesh(String fileName) throws Exception {
    	//// --- student code ---
    	
        float[] positions = null;
        //float[] textCoords = null;
        //float[] norms = null;
        int[] indices = null;
        
        float[] vtlist = null;
        float[] vnlist = null;
        int[] vtindex = null;
        int[] vnindex = null;
        int vcount=0;
        int fcount=0;
        int tmp1,tmp2,tmp3;
        int type=0;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
        	if (line.startsWith("#")) {	//ignore comment lines
                continue;
            }
        	if (line.startsWith("v ")) {
        		line=line.replaceAll(" +"," "); //replace multiple space with only one
        		String[] v = line.split(" ");
        		positions = ArrayUtils.add(positions, Float.valueOf(v[1]));
        		positions = ArrayUtils.add(positions, Float.valueOf(v[2]));
        		positions = ArrayUtils.add(positions, Float.valueOf(v[3]));
        		vcount=vcount+1;
        	}
        	if (line.startsWith("vt ")) {
        		line=line.replaceAll(" +"," ");
        		String[] v = line.split(" ");
        		vtlist = ArrayUtils.add(vtlist, Float.valueOf(v[1]));//put into temporary array first
        		vtlist = ArrayUtils.add(vtlist, Float.valueOf(v[2]));
        	}
        	if (line.startsWith("vn ")) {
        		line=line.replaceAll(" +"," ");
        		String[] v = line.split(" ");
        		vnlist = ArrayUtils.add(vnlist, Float.valueOf(v[1]));//put into temporary array first
        		vnlist = ArrayUtils.add(vnlist, Float.valueOf(v[2]));
        		vnlist = ArrayUtils.add(vnlist, Float.valueOf(v[3]));
        	}
        		
        	if (line.startsWith("f ")) {
        		line=line.replaceAll(" +"," ");
        		fcount=fcount+1;
                if (line.contains("//")){//if the f line is in form of ""x//x x//x x//x"
                	type=2;
                	String[] v = line.split(" ");
                	
                	String pt=v[1];
                	String[] ptl = pt.split("//");
                	tmp1=Integer.valueOf(ptl[0])-1;
                	indices = ArrayUtils.add(indices, tmp1);
                	tmp2=Integer.valueOf(ptl[1])-1;
                	
                	vnindex = ArrayUtils.add(vnindex, tmp2);//put into temporary array first
                	
                	pt=v[2];
                	ptl = pt.split("//");
                	tmp1=Integer.valueOf(ptl[0])-1;
                	indices = ArrayUtils.add(indices, tmp1);
                	tmp2=Integer.valueOf(ptl[1])-1;
                	vnindex = ArrayUtils.add(vnindex, tmp2);

                	pt=v[3];
                	ptl = pt.split("//");
                	tmp1=Integer.valueOf(ptl[0])-1;
                	indices = ArrayUtils.add(indices, tmp1);
                	tmp2=Integer.valueOf(ptl[1])-1;
                	vnindex = ArrayUtils.add(vnindex, tmp2);

                } else if (line.contains("/")){//if the f line is in form of ""x/x/x x/x/x x/x/x"
                	type=3;
                	String[] v = line.split(" ");
                	String pt=v[1];
                	String[] ptl = pt.split("/");
                	tmp1=Integer.valueOf(ptl[0])-1;
                	indices = ArrayUtils.add(indices, tmp1);
                	tmp2=Integer.valueOf(ptl[1])-1;
                	vtindex = ArrayUtils.add(vtindex, tmp2);//put into temporary array first
                	tmp3=Integer.valueOf(ptl[2])-1;
                	vnindex = ArrayUtils.add(vnindex, tmp3);
                	
                	pt=v[2];
                	ptl = pt.split("/");
                	tmp1=Integer.valueOf(ptl[0])-1;
                	indices = ArrayUtils.add(indices, tmp1);
                	tmp2=Integer.valueOf(ptl[1])-1;
                	vtindex = ArrayUtils.add(vtindex, tmp2);
                	tmp3=Integer.valueOf(ptl[2])-1;
                	vnindex = ArrayUtils.add(vnindex, tmp3);
                	
                	pt=v[3];
                	ptl = pt.split("/");
                	tmp1=Integer.valueOf(ptl[0])-1;
                	indices = ArrayUtils.add(indices, tmp1);
                	tmp2=Integer.valueOf(ptl[1])-1;
                	vtindex = ArrayUtils.add(vtindex, tmp2);
                	tmp3=Integer.valueOf(ptl[2])-1;
                	vnindex = ArrayUtils.add(vnindex, tmp3);
                } else{//if the f line is in form of ""x x x"
                	type=1;
                	String[] v = line.split(" ");
                	tmp1=Integer.valueOf(v[1])-1;
                	indices = ArrayUtils.add(indices, tmp1);
                	tmp2=Integer.valueOf(v[2])-1;
                	indices = ArrayUtils.add(indices, tmp2);
                	tmp3=Integer.valueOf(v[3])-1;
                	indices = ArrayUtils.add(indices, tmp3);
                }
                
        	}
        	
        }
        float[] norms = new float[vcount*3];
        float[] textCoords = new float[vcount*2];
        
        //System.out.println(vcount);
        if (type==1)
        {
			for(int i=0;i<fcount;i++){
				for(int j=0;j<3;j++){
					int vid=indices[3*i+j];
					textCoords[2*vid]=0;
					textCoords[2*vid+1]=0;
					norms[3*vid] = 0;
					norms[3*vid+1] = 0;
					norms[3*vid+2] = 0;
				}
			}
        }else if (type==2){
        	for(int i=0;i<fcount;i++){
				for(int j=0;j<3;j++){
					int vid=indices[3*i+j];
					
					int nid=vnindex[3*i+j];
					textCoords[2*vid]=0;
					textCoords[2*vid+1]=0;
					norms[3*vid] = vnlist[3*nid];
					norms[3*vid+1] = vnlist[3*nid+1];
					norms[3*vid+2] = vnlist[3*nid+2];
				}
			}
        }else if (type==3){
        	for(int i=0;i<fcount;i++){
				for(int j=0;j<3;j++){      //based on the id that we put into temporary array 
					int vid=indices[3*i+j];// and the vertex indices now we can form norms array and textCoords array
					int tid=vtindex[3*i+j];
					int nid=vnindex[3*i+j];
					textCoords[2*vid]=vtlist[2*tid];
					textCoords[2*vid+1]=vtlist[2*tid+1];
					norms[3*vid] = vnlist[3*nid];
					norms[3*vid+1] = vnlist[3*nid+1];
					norms[3*vid+2] = vnlist[3*nid+2];
				}
			}
        }
        
        reader.close();
        return new Mesh(positions, textCoords, norms, indices);
    }
	

}
