package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.influencers.ColorInfluencer;
import com.epaga.particles.influencers.SizeInfluencer;
import com.epaga.particles.influencers.VelocityInfluencer;
import com.epaga.particles.valuetypes.*;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class OrbitalVelocityExample extends ParticleExample {

  public OrbitalVelocityExample(AssetManager assetManager) {
    super("Orbital Velocity Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        new ColorInfluencer(),
        new SizeInfluencer(),
        new VelocityInfluencer()
    );

    Curve y = new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 3.0f), new Vector2f(0.2f, 3.0f))
        .addControlPoint(new Vector2f(0.8f, 3.0f), new Vector2f(1.0f, 3.0f), null);

    Curve xz = new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 1.0f), new Vector2f(0.2f, 1.0f))
        .addControlPoint(new Vector2f(0.8f, 1.0f), new Vector2f(1.0f, 1.0f), null);

    VectorValueType valueType = new VectorValueType();
    valueType.setCurve(xz.clone(), y, xz);
    emitter.getInfluencer(VelocityInfluencer.class).setOrbital(valueType);
    emitter.getInfluencer(VelocityInfluencer.class).setOrbitalRotations(new VectorValueType(new Vector3f(8, 8, 8)));

    emitter.getInfluencer(SizeInfluencer.class).setSizeOverTime(new ValueType(new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 0.0f), new Vector2f(0.3f, 0.0f))
        .addControlPoint(new Vector2f(0.3f, 1.0f), new Vector2f(0.5f, 1.0f), new Vector2f(0.7f, 1.0f))
        .addControlPoint(new Vector2f(0.7f, 0.0f), new Vector2f(1.0f, 0.0f), null)
    ));

    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(
        new Gradient().addGradPoint(new ColorRGBA(0.8f, 0.6f, 0.0f, 0.6f), 0.0f)
            .addGradPoint(new ColorRGBA(0.8f, 0.0f, 0.0f, 0.5f), 0.8f)
            .addGradPoint(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.0f), 1.0f)
    ));

    emitter.setStartSize(new ValueType(0.7f));
    emitter.setStartSpeed(new ValueType(3.5f));
    emitter.setLifeMinMax(new ValueType(3.0f), new ValueType(3.0f));
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(5);
    emitter.setShape(new EmitterCone());

    emitter.setLocalTranslation(-0.5f, 0.5f, -0.5f);
    rootNode.attachChild(emitter);
  }
}
