package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCircle;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.influencers.ColorInfluencer;
import com.epaga.particles.influencers.SizeInfluencer;
import com.epaga.particles.influencers.VelocityInfluencer;
import com.epaga.particles.valuetypes.*;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public class VelocityExample extends ParticleExample {

  public VelocityExample(AssetManager assetManager) {
    super("Velocity Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        new ColorInfluencer(),
        new VelocityInfluencer()
    );

    Curve x = new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 1.0f), new Vector2f(0.2f, 1.0f))
        .addControlPoint(new Vector2f(0.2f, 1.0f), new Vector2f(0.3f, 1.0f), new Vector2f(0.5f, 1.0f))
        .addControlPoint(new Vector2f(0.3f, 0.1f), new Vector2f(0.4f, 0.1f), new Vector2f(0.6f, 0.1f))
        .addControlPoint(new Vector2f(0.8f, 0.0f), new Vector2f(1.0f, 0.0f), null);

    VectorValueType valueType = new VectorValueType();
    valueType.setCurve(x, x.clone(), x.clone());
    emitter.getInfluencer(VelocityInfluencer.class).setLinear(valueType);

    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(
        new Gradient().addGradPoint(new ColorRGBA(0.8f, 0.6f, 0.0f, 0.6f), 0.0f)
            .addGradPoint(new ColorRGBA(0.8f, 0.0f, 0.0f, 0.5f), 0.8f)
            .addGradPoint(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.0f), 1.0f)
    ));

    emitter.setStartSpeed(new ValueType(20.5f));
    emitter.setLifeMinMax(new ValueType(1.0f), new ValueType(1.0f));
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(20);
    EmitterCircle circle = new EmitterCircle();
    circle.setOriginDirection(1.0f);
    emitter.setShape(circle);

    emitter.setLocalTranslation(0, 0.5f, 0);
    rootNode.attachChild(emitter);
  }
}
