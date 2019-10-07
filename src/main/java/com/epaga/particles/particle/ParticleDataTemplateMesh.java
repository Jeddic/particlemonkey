/*
 * Copyright (c) 2019 Greg Hoffman
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.epaga.particles.particle;

import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.epaga.particles.Emitter;
import com.jme3.renderer.Camera;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * ParticleDataTemplateMesh
 *
 * Use a template mesh for each particle instead a quad or a point
 *
 * @author t0neg0d
 * @author Jedic
 */
public class ParticleDataTemplateMesh extends ParticleDataMesh {

  private int imagesX = 1;
  private int imagesY = 1;
  private boolean uniqueTexCoords = false;
  private Emitter emitter;
  private Vector3f left = new Vector3f(), tempLeft = new Vector3f();
  private Vector3f up = new Vector3f(), tempUp = new Vector3f();
  private Vector3f dir = new Vector3f();
  private Vector3f tempV3 = new Vector3f();
  private Quaternion rotStore = new Quaternion();
  private Quaternion tempQ = new Quaternion();
  private Node tempN = new Node();
  private int imgX, imgY;
  private float startX, startY, endX, endY;
  private Mesh template;
  private FloatBuffer templateVerts;
  private FloatBuffer templateCoords;
  private IndexBuffer templateIndexes;
  private FloatBuffer templateNormals;
  private FloatBuffer templateColors;
  private FloatBuffer finVerts;
  private FloatBuffer finCoords;
  private ShortBuffer finIndexes;
  private FloatBuffer finNormals;
  private FloatBuffer finColors;

  @Override
  public void extractTemplateFromMesh(Mesh mesh) {
    template = mesh;
    templateVerts = MeshUtils.getPositionBuffer(mesh);
    templateCoords = MeshUtils.getTexCoordBuffer(mesh);
    templateIndexes = MeshUtils.getIndexBuffer(mesh);
    templateNormals = MeshUtils.getNormalsBuffer(mesh);
    templateColors = BufferUtils.createFloatBuffer(templateVerts.capacity() / 3 * 4);
  }

  public Mesh getTemplateMesh() {
    return this.template;
  }

  @Override
  public void initParticleData(Emitter emitter, int numParticles) {
    setMode(Mesh.Mode.Triangles);

    this.emitter = emitter;

    this.finVerts = BufferUtils.createFloatBuffer(templateVerts.capacity() * numParticles + 3);
    Vector3f worldLoc = emitter.getWorldTranslation();
    finVerts.put(templateVerts.capacity() * numParticles, worldLoc.x);
    finVerts.put(templateVerts.capacity() * numParticles + 1, worldLoc.y);
    finVerts.put(templateVerts.capacity() * numParticles + 2, worldLoc.z);

    try {
      this.finCoords = BufferUtils.createFloatBuffer(templateCoords.capacity() * numParticles);
    } catch (Exception e) {
    }
    this.finIndexes = BufferUtils.createShortBuffer(templateIndexes.size() * numParticles);
    this.finNormals = BufferUtils.createFloatBuffer(templateNormals.capacity() * numParticles);
    this.finColors = BufferUtils.createFloatBuffer(templateVerts.capacity() / 3 * 4 * numParticles);
    
    int index = 0, index2 = 0, index3 = 0, index4 = 0;
    int indexOffset = 0;

    for (int i = 0; i < numParticles; i++) {
      templateVerts.rewind();
      for (int v = 0; v < templateVerts.capacity(); v += 3) {
        tempV3.set(templateVerts.get(v), templateVerts.get(v + 1), templateVerts.get(v + 2));
        finVerts.put(index, tempV3.getX());
        index++;
        finVerts.put(index, tempV3.getY());
        index++;
        finVerts.put(index, tempV3.getZ());
        index++;
      }
      try {
        templateCoords.rewind();
        for (int v = 0; v < templateCoords.capacity(); v++) {
          finCoords.put(index2, templateCoords.get(v));
          index2++;
        }
      } catch (Exception e) {
      }
      for (int v = 0; v < templateIndexes.size(); v++) {
        finIndexes.put(index3, (short) (templateIndexes.get(v) + indexOffset));
        index3++;
      }
      indexOffset += templateVerts.capacity() / 3;

      templateNormals.rewind();
      for (int v = 0; v < templateNormals.capacity(); v++) {
        finNormals.put(index4, templateNormals.get(v));
        index4++;
      }
    }

    // Help GC
    //	tempV3 = null;
    //	templateVerts = null;
    //	templateCoords = null;
    //	templateIndexes = null;
    //	templateNormals = null;

    // Clear & ssign buffers
    this.clearBuffer(VertexBuffer.Type.Position);
    this.setBuffer(VertexBuffer.Type.Position, 3, finVerts);
    this.clearBuffer(VertexBuffer.Type.TexCoord);
    try {
      this.setBuffer(VertexBuffer.Type.TexCoord, 2, finCoords);
    } catch (Exception e) {
    }
    this.clearBuffer(VertexBuffer.Type.Index);
    this.setBuffer(VertexBuffer.Type.Index, 3, finIndexes);
    this.clearBuffer(VertexBuffer.Type.Normal);
    this.setBuffer(VertexBuffer.Type.Normal, 3, finNormals);
    
    clearBuffer(VertexBuffer.Type.Color);
    setBuffer(VertexBuffer.Type.Color, 4, finColors);
    this.updateBound();
  }

  @Override
  public void setImagesXY(int imagesX, int imagesY) {
    this.imagesX = imagesX;
    this.imagesY = imagesY;
    if (imagesX != 1 || imagesY != 1) {
      uniqueTexCoords = true;
      getBuffer(VertexBuffer.Type.TexCoord).setUsage(Usage.Stream);
    }
  }

  public int getSpriteCols() {
    return this.imagesX;
  }

  public int getSpriteRows() {
    return this.imagesY;
  }

  @Override
  public void updateParticleData(ParticleData[] particles, Camera cam, Matrix3f inverseRotation) {
    //    VertexBuffer pvb = getBuffer(VertexBuffer.Type.Position);
    //   FloatBuffer positions = (FloatBuffer) pvb.getData();

//       VertexBuffer cvb = getBuffer(VertexBuffer.Type.Color);
//       ByteBuffer colors = (ByteBuffer) cvb.getData();

    //   VertexBuffer tvb = getBuffer(VertexBuffer.Type.TexCoord);
    //   FloatBuffer texcoords = (FloatBuffer) tvb.getData();

    // update data in vertex buffers
    //   positions.clear();
    //	positions.rewind();
    //   colors.clear();
    //   texcoords.clear();

    for (int i = 0; i < particles.length; i++) {
      ParticleData p = particles[i];
      int offset = templateVerts.capacity() * i;
      int colorOffset = templateColors.capacity() * i;
      if (p.life == 0) {
        for (int x = 0; x < templateVerts.capacity(); x++) {
          finVerts.put(offset + x, 0);
        }
        continue;
      }
      for (int x = 0; x < templateVerts.capacity(); x += 3) {
        tempV3.set(templateVerts.get(x), templateVerts.get(x + 1), templateVerts.get(x + 2));
        rotStore = tempQ.fromAngleAxis(p.angles.y, p.velocity);
        tempV3 = rotStore.mult(tempV3);

        rotStore = tempQ.fromAngleAxis(p.angles.x, p.velocity);
        tempV3 = rotStore.mult(tempV3);

        rotStore = tempQ.fromAngleAxis(p.angles.z, p.velocity);
        tempV3 = rotStore.mult(tempV3);

        tempV3.multLocal(p.size);
        tempV3.addLocal(p.position);

        finVerts.put(offset + x, tempV3.getX());
        finVerts.put(offset + x + 1, tempV3.getY());
        finVerts.put(offset + x + 2, tempV3.getZ());
      }

      /*
       if (particles.getParticlesFollowEmitter()) {
       tempV3.set(p.position);
       } else {
       tempV3.set(p.position).subtractLocal(particles.getNode().getWorldTranslation().subtract(p.initialPosition).divide(8f));
       }
			
       positions.put(tempV3.x + left.x + up.x)
       .put(tempV3.y + left.y + up.y)
       .put(tempV3.z + left.z + up.z);

       positions.put(tempV3.x - left.x + up.x)
       .put(tempV3.y - left.y + up.y)
       .put(tempV3.z - left.z + up.z);

       positions.put(tempV3.x + left.x - up.x)
       .put(tempV3.y + left.y - up.y)
       .put(tempV3.z + left.z - up.z);

       positions.put(tempV3.x - left.x - up.x)
       .put(tempV3.y - left.y - up.y)
       .put(tempV3.z - left.z - up.z);

       if (uniqueTexCoords){
       imgX = p.spriteCol;
       imgY = p.spriteRow;

       startX = 1f/imagesX*imgX;
       startY = 1f/imagesY*imgY;
       endX   = startX + 1f/imagesX;
       endY   = startY + 1f/imagesY;

       texcoords.put(startX).put(endY);
       texcoords.put(endX).put(endY);
       texcoords.put(startX).put(startY);
       texcoords.put(endX).put(startY);
       }
       */

       for (int v = 0; v < templateColors.capacity(); v += 4) {
            finColors.put(colorOffset + v, p.color.r)
                    .put(colorOffset + v + 1, p.color.g)
                    .put(colorOffset + v + 2, p.color.b)
                    .put(colorOffset + v + 3, p.color.a);
        }
    }

    this.clearBuffer(VertexBuffer.Type.Position);
    this.setBuffer(VertexBuffer.Type.Position, 3, finVerts);
    setBuffer(VertexBuffer.Type.Color, 4, finColors);
    
    //	this.setBuffer(VertexBuffer.Type.Position, 3, positions);
    //    positions.clear();
    //    if (!uniqueTexCoords)
    //        texcoords.clear();
    //    else{
    //        texcoords.clear();
    //        tvb.updateData(texcoords);
    //    }

    // force renderer to re-send data to GPU
    //    pvb.updateData(positions);
    //    cvb.updateData(colors);

    updateBound();
  }
}