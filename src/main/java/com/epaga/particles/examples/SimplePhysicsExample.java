package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.influencers.BasicPhysicsInfluencer;
import com.epaga.particles.influencers.ColorInfluencer;
import com.epaga.particles.influencers.GravityInfluencer;
import com.epaga.particles.influencers.SizeInfluencer;
import com.epaga.particles.particle.ParticleDataPointMesh;
import com.epaga.particles.valuetypes.ColorValueType;
import com.epaga.particles.valuetypes.Curve;
import com.epaga.particles.valuetypes.Gradient;
import com.epaga.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.io.IOException;

public class SimplePhysicsExample extends ParticleExample {

  public SimplePhysicsExample(AssetManager assetManager) {
    super("Simple Physics", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        //new VelocityInfluencer(),
        new ColorInfluencer(),
        new SizeInfluencer(),
        new GravityInfluencer(),
        new BasicPhysicsInfluencer(collisionShape)
    );

    emitter.getInfluencer(SizeInfluencer.class).setSizeOverTime(new ValueType(
        new Curve()
            .addControlPoint(null, new Vector2f(0, 1), new Vector2f(0.4f, 1.0f))
            .addControlPoint(new Vector2f(0.4f, 1.0f), new Vector2f(1.0f, 1), null)
    ));
    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(
        new Gradient()
            .addGradPoint(new ColorRGBA(1, 1, 1, 1.0f), 0.0f)
            .addGradPoint(new ColorRGBA(1, 1, 1, 1.0f), 0.8f)
            .addGradPoint(new ColorRGBA(1, 1, 1, 0.0f), 1.0f)
    ));

    emitter.setStartSize(new ValueType(0.2f));

    emitter.setStartSpeed(new ValueType(6.5f));
    //emitter.getInfluencer(SizeInfluencer.class).setMinMax(0.5f, 1.0f);
    emitter.setLifeMinMax(new ValueType(2.0f), new ValueType(2.0f));
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(1);
    emitter.setEnabled(true);
    emitter.setShape(new EmitterCone());
    ((EmitterCone)emitter.getShape()).setRadius(0.005f);
    emitter.rotate(0, 0, FastMath.PI * 0.8f);
    emitter.addControl(new Control() {
      private Spatial parent;
      @Override
      public Control cloneForSpatial(Spatial spatial) {
        return null;
      }

      @Override
      public void setSpatial(Spatial spatial) {
        parent = spatial;
      }

      @Override
      public void update(float v) {
        parent.rotate(0, 0, v);
      }

      @Override
      public void render(RenderManager renderManager, ViewPort viewPort) {

      }

      @Override
      public void write(JmeExporter jmeExporter) throws IOException {

      }

      @Override
      public void read(JmeImporter jmeImporter) throws IOException {

      }
    });
    emitter.setParticleMeshType(ParticleDataPointMesh.class, null);
    emitter.setParticlesFollowEmitter(false);
    emitter.getMaterial().setBoolean("PointSprite", true);
    emitter.setQueueBucket(RenderQueue.Bucket.Translucent);

    emitter.setLocalTranslation(0, 3.0f, 0);
    rootNode.attachChild(emitter);
  }
}
