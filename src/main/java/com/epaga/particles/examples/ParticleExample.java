package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.jme3.anim.tween.Tween;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;


public abstract class ParticleExample {
  private String name = "Example";
  private Boolean tweens = false;
  protected AssetManager assetManager;
  protected Emitter emitter;
  protected Tween emitterTween;
  protected Geometry collisionShape;

  public ParticleExample(String n, boolean tweens, AssetManager assetManager) {
    this.name = n;
    this.tweens = tweens;
    this.assetManager = assetManager;
  }

  public abstract void addEmitterToNode(Node rootNode);
  public Emitter getEmitter() {
    return emitter;
  }

  public String getName() {
    return name;
  }

  public boolean hasTweenCtrl() {
    return tweens;
  }

  public Tween getEmitterTween() {
    return emitterTween;
  }

  public void setCollisionShape(Geometry collisionShape) {
    this.collisionShape = collisionShape;
  }

  protected Material getPartMat(String path) {
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
    Texture tex = assetManager.loadTexture(path);
    mat.setTexture("Texture", tex);
    return mat;
  }
}
