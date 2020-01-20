package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.influencers.*;
import com.epaga.particles.valuetypes.ColorValueType;
import com.epaga.particles.valuetypes.Curve;
import com.epaga.particles.valuetypes.Gradient;
import com.epaga.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public class TrailExample extends ParticleExample {

  public TrailExample(AssetManager assetManager) {
    super("Trail Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        //new VelocityInfluencer(),
        new ColorInfluencer(),
        new SizeInfluencer(),
        new GravityInfluencer(),
        new TrailInfluencer(),
        new BasicPhysicsInfluencer(collisionShape)
    );

    emitter.getInfluencer(TrailInfluencer.class).setTrailSize(new ValueType(0.05f));
    emitter.getInfluencer(TrailInfluencer.class).setTrailLife(0.5f);
    emitter.getInfluencer(TrailInfluencer.class).setTrailmat(getPartMat("Effects/Particles/part_beam.png"));
    emitter.getInfluencer(TrailInfluencer.class).setColorOverLifetime(new ColorValueType(new Gradient()
        .addGradPoint(new ColorRGBA(1, 1, 1, 1), 0.0f)
        .addGradPoint(new ColorRGBA(1, 1, 1, 0.5f), 0.8f)
        .addGradPoint(new ColorRGBA(1, 1, 1, 0.0f), 1.0f)
    ));

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
    emitter.setLifeMinMax(new ValueType(2.0f), new ValueType(2.0f));
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(1);
    emitter.setShape(new EmitterCone());
    ((EmitterCone)emitter.getShape()).setRadius(0.005f);
    emitter.setLocalTranslation(0, 2.0f, 0);
    rootNode.attachChild(emitter);
  }
}
