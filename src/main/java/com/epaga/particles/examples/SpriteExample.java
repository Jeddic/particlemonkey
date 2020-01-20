package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.emittershapes.EmitterLine;
import com.epaga.particles.influencers.*;
import com.epaga.particles.valuetypes.*;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class SpriteExample extends ParticleExample {

  public SpriteExample(AssetManager assetManager) {
    super("Sprite Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_flame.png"), 200,
        new ColorInfluencer(),
        new SpriteInfluencer(),
        new SizeInfluencer(),
        new RandomInfluencer(),
        new RotationLifetimeInfluencer(),
        new VelocityInfluencer()
    );

    Curve x = new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 1.0f), new Vector2f(0.2f, 1.0f))
        .addControlPoint(new Vector2f(0.8f, 0.0f), new Vector2f(1.0f, 0.0f), null);

    VectorValueType valueType = new VectorValueType();
    valueType.setCurve(x, x.clone(), x.clone());
    emitter.getInfluencer(VelocityInfluencer.class).setLinear(valueType);
    emitter.getInfluencer(SpriteInfluencer.class).setSpriteCols(2);
    emitter.getInfluencer(SpriteInfluencer.class).setSpriteRows(2);
    emitter.getInfluencer(RotationLifetimeInfluencer.class).setSpeedOverLifetime(new VectorValueType(new Vector3f(0, 0, 3.0f)));

    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(
        new Gradient().addGradPoint(new ColorRGBA(0.8f, 0.6f, 0.0f, 0.6f), 0.0f)
            .addGradPoint(new ColorRGBA(0.8f, 0.0f, 0.0f, 0.5f), 0.8f)
            .addGradPoint(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.0f), 1.0f)
    ));


    emitter.getInfluencer(SizeInfluencer.class).setSizeOverTime(new ValueType(new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 1.0f), new Vector2f(0.3f, 1.0f))
        .addControlPoint(new Vector2f(0.7f, 0.0f), new Vector2f(1.0f, 0.0f), null)
    ));

    emitter.setStartSpeed(new ValueType(6.5f));
    emitter.setLifeMinMax(new ValueType(0.5f), new ValueType(1.0f));
    emitter.setEmissionsPerSecond(30);
    emitter.setParticlesPerEmission(4);
    emitter.setShape(new EmitterLine(3));
    emitter.setStartRotation(new VectorValueType(new Vector3f(0, 0, -FastMath.PI), new Vector3f(0, 0, FastMath.PI)));

    emitter.setLocalTranslation(0, 0.5f, 0);
    rootNode.attachChild(emitter);
  }
}
