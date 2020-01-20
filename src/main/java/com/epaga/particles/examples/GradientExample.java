package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.influencers.ColorInfluencer;
import com.epaga.particles.valuetypes.ColorValueType;
import com.epaga.particles.valuetypes.Gradient;
import com.epaga.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;


public class GradientExample extends ParticleExample {
  public GradientExample(AssetManager assetManager) {
    super("Gradient Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        new ColorInfluencer());

    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(
        new Gradient().addGradPoint(new ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f), 0.0f)
            .addGradPoint(new ColorRGBA(0.0f, 1.0f, 0.0f, 1.0f), 0.5f)
            .addGradPoint(new ColorRGBA(0.0f, 0.0f, 1.0f, 1.0f), 1.0f)
    ));

    emitter.setStartSpeed(new ValueType(4.5f));
    emitter.setLifeMinMax(new ValueType(0.9f), new ValueType(0.9f));
    emitter.setEmissionsPerSecond(5);
    emitter.setParticlesPerEmission(1);
    emitter.setShape(new EmitterCone());

    emitter.setLocalTranslation(0, 0.5f, 0);
    rootNode.attachChild(emitter);
  }
}
