package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.influencers.SizeInfluencer;
import com.epaga.particles.valuetypes.Curve;
import com.epaga.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public class SizeExample extends ParticleExample {

  public SizeExample(AssetManager assetManager) {
    super("Size Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        new SizeInfluencer()
    );

    emitter.setStartSize(new ValueType(0.5f));
    emitter.getInfluencer(SizeInfluencer.class).setSizeOverTime(new ValueType(new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 0.0f), new Vector2f(0.3f, 0.0f))
        .addControlPoint(new Vector2f(0.3f, 1.0f), new Vector2f(0.5f, 1.0f), new Vector2f(0.7f, 1.0f))
        .addControlPoint(new Vector2f(0.7f, 0.0f), new Vector2f(1.0f, 0.0f), null)
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
