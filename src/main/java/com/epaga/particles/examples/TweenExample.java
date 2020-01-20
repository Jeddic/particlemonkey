package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.ParticleHelper;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public class TweenExample extends ParticleExample {

  public TweenExample(AssetManager assetManager) {
    super("Tween Example", true, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100);

    emitter.setStartSize(new ValueType(0.2f));
    emitter.setStartSpeed(new ValueType(0.0f));
    emitter.setLifeMinMax(new ValueType(2.0f), new ValueType(2.0f));
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(1);
    emitter.setShape(new EmitterCone());
    ((EmitterCone)emitter.getShape()).setRadius(0.005f);

    emitterTween = ParticleHelper.startSpeed(emitter, 1.0, 0.0f, 6.5f);

    emitter.setLocalTranslation(0, 0.5f, 0);
    rootNode.attachChild(emitter);
  }
}
