package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.ParticleHelper;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.emittershapes.EmitterMesh;
import com.epaga.particles.emittershapes.EmitterSphere;
import com.epaga.particles.influencers.*;
import com.epaga.particles.valuetypes.*;
import com.jme3.asset.AssetManager;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class TimeOrbExample extends ParticleExample {

  public TimeOrbExample(AssetManager assetManager) {
    super("Time Orb", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {

    Node test = new Node("TimeOrb");
    rootNode.attachChild(test);
    Emitter emitter = new Emitter("orb", getPartMat("Effects/Particles/part_circle_glow.png"), 3,
        new ColorInfluencer(),
        new SizeInfluencer()
    );
    emitter.setStartSpeed(new ValueType(0.0f));
    ParticleHelper.minMaxSize(emitter, 2.0f, 2.4f);
    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(new Gradient()
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.0f), 1.0f)
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.8f), 0.8f)
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.6f), 0.5f)
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.0f), 0.0f)
    ));

    emitter.setLifeMinMax(new ValueType(1.0f), new ValueType(1.0f));
    emitter.setEmissionsPerSecond(2);
    emitter.setParticlesPerEmission(1);
    emitter.setEnabled(true);
    //emitter.setSprite("Effects/Particles/part_circle_glow.png", 256, 256);
    //particles.getRenderInfo().mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
    Mesh box = new Box(0.01f, 0.01f, 0.01f);
    EmitterSphere sphere = new EmitterSphere();
    sphere.setRadius(0.001f);
    emitter.setShape(sphere);
    test.attachChild(emitter);

    test.setLocalTranslation(0, 2, 0);

    emitter = new Emitter("orbLights",
        getPartMat("Effects/Particles/part_light.png"),
        50,
        new ColorInfluencer(),
        new TrailInfluencer(),
        new RandomInfluencer(),
        new SizeInfluencer()
    );
    emitter.getInfluencer(TrailInfluencer.class).setTrailmat(getPartMat("Effects/Particles/part_beam.png"));
    emitter.setStartSpeed(new ValueType(8.0f));
    ParticleHelper.minMaxSize(emitter, 0.2f, 0.3f);
    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(new Gradient()
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.0f), 1.0f)
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.6f), 0.8f)
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.8f), 0.5f)
        .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.0f), 0.0f)
    ));
    emitter.getInfluencer(RandomInfluencer.class).setMagnitude(0.15f);
    TrailInfluencer trail = emitter.getInfluencer(TrailInfluencer.class);
    trail.setTrailSize(new ValueType(0.05f));
    trail.setUseParticleColor(true);

//        .setColorOverLifetime(new Gradient()
//            .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.0f), 1.0f)
//            .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.8f), 0.8f)
//            .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.8f), 0.5f)
//            .addGradPoint(new ColorRGBA(0.6f, 0.1f, 0.95f, 0.8f), 0.0f));

    emitter.setLifeMinMax(new ValueType(0.25f), new ValueType(0.5f));
    emitter.setEmissionsPerSecond(30);
    emitter.setParticlesPerEmission(2);
    emitter.setEnabled(true);
    //emitter.setSprite("Effects/Particles/part_light.png", 128, 128);
    //particles.getRenderInfo().mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
    box = new Sphere(12, 12, 0.2f);
    emitter.setShape(new EmitterMesh(box));
    test.attachChild(emitter);

    emitter = new Emitter("orbElectric",
        getPartMat("Effects/Particles/part_light.png"),
        4,
        new ColorInfluencer(),
        new SizeInfluencer(),
        new RotationLifetimeInfluencer()
    );
    emitter.setStartSpeed(new ValueType(0.0f));
    emitter.setStartSize(new ValueType(1.8f));
    ParticleHelper.minMaxSize(emitter, 1.8f, 2.0f);
    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(new Gradient()
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.0f), 1.0f)
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.8f), 0.8f)
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.8f), 0.5f)
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.0f), 0.0f)
    ));
    emitter.getInfluencer(RotationLifetimeInfluencer.class).setSpeedOverLifetime(new VectorValueType(new Vector3f(0, 0, 3)));

    emitter.setLifeMinMax(new ValueType(1.0f), new ValueType(1.0f));
    emitter.setEmissionsPerSecond(4);
    emitter.setParticlesPerEmission(1);
    emitter.setEnabled(true);
    emitter.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
    box = new Box(0.01f, 0.01f, 0.01f);
    emitter.setShape(new EmitterMesh(box));
    test.attachChild(emitter);

    emitter = new Emitter("orbClouds",
        getPartMat("Effects/Particles/part_light.png"),
        50,
        new ColorInfluencer(),
        new SizeInfluencer(),
        new RotationLifetimeInfluencer()
    );
    emitter.setStartSpeed(new ValueType(0.0f));
    ParticleHelper.minMaxSize(emitter, 1.8f, 0.3f);
    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(new Gradient()
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.0f), 1.0f)
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.3f), 0.8f)
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.3f), 0.5f)
        .addGradPoint(new ColorRGBA(0.6f, 0.4f, 0.95f, 0.0f), 0.0f)
    ));
    emitter.getInfluencer(RotationLifetimeInfluencer.class).setSpeedOverLifetime(new VectorValueType(new Vector3f(0, 0, 1)));

    emitter.setLifeMinMax(new ValueType(1.0f), new ValueType(1.0f));
    emitter.setEmissionsPerSecond(30);
    emitter.setParticlesPerEmission(1);
    emitter.setEnabled(true);
    emitter.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
    box = new Sphere(12, 12, 2.2f);
    emitter.setShape(new EmitterMesh(box));
    test.attachChild(emitter);

  }
}
