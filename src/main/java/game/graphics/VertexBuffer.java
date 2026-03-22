package game.graphics;

import game.utils.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.*;
import static org.lwjgl.assimp.Assimp.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL43.*;

public class VertexBuffer {


    /*
    * Vertex layout:
    * position: 3 floats
    * normal: 3 floats
    * uv: 2 floats
    * */
    private FloatBuffer vertex_data;
    private IntBuffer index_data;
    public static int VERTEX_SIZE = 8;


    public VertexBuffer(FloatBuffer buffer) {
        vertex_data = buffer;
    }

    public VertexBuffer(float[] vertices, int[] indices) {
        vertex_data = BufferUtils.createFloatBuffer(vertices.length);
        vertex_data.put(vertices);
        vertex_data.flip();

        index_data = BufferUtils.createIntBuffer(indices.length);
        index_data.put(indices);
        index_data.flip();
    }

    public static VertexBuffer loadObj(String path) {
        AIScene scene = aiImportFile(path,
                aiProcess_Triangulate       |  // auto-triangulate quads/ngons
                        aiProcess_GenSmoothNormals  |  // generate normals if missing
                        aiProcess_FlipUVs           |  // OpenGL expects Y-flipped UVs
                        aiProcess_JoinIdenticalVertices
        );

        if (scene == null || (scene.mFlags() & AI_SCENE_FLAGS_INCOMPLETE) != 0) {
            throw new RuntimeException("Assimp failed to load: " + aiGetErrorString());
        }

        // Just grab the first mesh for a simple loader
        AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));

        int vertexCount = mesh.mNumVertices();
        float[] vertices = new float[vertexCount * VERTEX_SIZE];

        AIVector3D.Buffer positions = mesh.mVertices();
        AIVector3D.Buffer normals   = mesh.mNormals();
        AIVector3D.Buffer uvs       = mesh.mTextureCoords(0); // channel 0

        for (int i = 0; i < vertexCount; i++) {
            int offset = i * VERTEX_SIZE;

            AIVector3D pos = positions.get(i);
            vertices[offset]     = pos.x();
            vertices[offset + 1] = pos.y();
            vertices[offset + 2] = pos.z();

            if (normals != null) {
                AIVector3D n = normals.get(i);
                vertices[offset + 3] = n.x();
                vertices[offset + 4] = n.y();
                vertices[offset + 5] = n.z();
            }

            if (uvs != null) {
                AIVector3D uv = uvs.get(i);
                vertices[offset + 6] = uv.x();
                vertices[offset + 7] = uv.y();
            }
        }

        // Unpack face indices
        int faceCount = mesh.mNumFaces();
        int[] indices = new int[faceCount * 3];
        AIFace.Buffer faces = mesh.mFaces();

        for (int i = 0; i < faceCount; i++) {
            AIFace face = faces.get(i);
            indices[i * 3]     = face.mIndices().get(0);
            indices[i * 3 + 1] = face.mIndices().get(1);
            indices[i * 3 + 2] = face.mIndices().get(2);
        }

        aiReleaseImport(scene); // free native memory

        return new VertexBuffer(vertices, indices);
    }


    public FloatBuffer getBuffer() {
        return vertex_data;
    }

    public IntBuffer getIndices() {
        return index_data;
    }

    public int indicesLength() {
        return index_data.remaining();
    }

    public static void setVertexAttributes() {
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * VERTEX_SIZE, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, Float.BYTES * VERTEX_SIZE, 3 * Float.BYTES);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, Float.BYTES * VERTEX_SIZE, 6 * Float.BYTES);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }
}
