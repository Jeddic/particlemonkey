package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;


public class SimpleExample extends ParticleExample {
  public SimpleExample(AssetManager assetManager) {
    super("Simple Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100);

    emitter.setStartSpeed(new ValueType(6.5f));
    emitter.setLifeFixedDuration(2.0f);
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(1);
    emitter.setShape(new EmitterCone());
    ((EmitterCone)emitter.getShape()).setRadius(0.005f);

    emitter.setLocalTranslation(0, 0.5f, 0);
    rootNode.attachChild(emitter);
  }
}
