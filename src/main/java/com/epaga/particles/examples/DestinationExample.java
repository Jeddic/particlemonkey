package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.influencers.PreferredDestinationInfluencer;
import com.epaga.particles.influencers.SizeInfluencer;
import com.epaga.particles.valuetypes.ColorValueType;
import com.epaga.particles.valuetypes.Curve;
import com.epaga.particles.valuetypes.ValueType;
import com.epaga.particles.valuetypes.VectorValueType;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class DestinationExample extends ParticleExample {

  public DestinationExample(AssetManager assetManager) {
    super("Destination Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        new SizeInfluencer(),
        new PreferredDestinationInfluencer()
    );

    emitter.getInfluencer(PreferredDestinationInfluencer.class)
        .setPreferredDestination(new VectorValueType(new Vector3f(5, 0, 0)));

    emitter.getInfluencer(SizeInfluencer.class).setSizeOverTime(new ValueType(new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 0.0f), new Vector2f(0.3f, 0.1f))
        .addControlPoint(new Vector2f(0.3f, 1.0f), new Vector2f(0.5f, 1.0f), new Vector2f(0.7f, 1.0f))
        .addControlPoint(new Vector2f(0.7f, 1.0f), new Vector2f(1.0f, 1.0f), null)
    ));

    emitter.setStartColor(new ColorValueType());
    emitter.setStartSize(new ValueType(0.7f));
    emitter.setStartSpeed(new ValueType(6.5f));
    emitter.setLifeMinMax(new ValueType(4.0f), new ValueType(4.0f));
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(5);
    emitter.setShape(new EmitterCone());

    emitter.setLocalTranslation(-2.5f, 0.5f, 0);
    rootNode.attachChild(emitter);
  }
}
