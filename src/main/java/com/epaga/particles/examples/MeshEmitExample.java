package com.epaga.particles.examples;

import com.epaga.particles.Emitter;
import com.epaga.particles.emittershapes.EmitterCone;
import com.epaga.particles.emittershapes.EmitterMesh;
import com.epaga.particles.influencers.ColorInfluencer;
import com.epaga.particles.influencers.SizeInfluencer;
import com.epaga.particles.valuetypes.ColorValueType;
import com.epaga.particles.valuetypes.Curve;
import com.epaga.particles.valuetypes.Gradient;
import com.epaga.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Torus;

public class MeshEmitExample extends ParticleExample {

  public MeshEmitExample(AssetManager assetManager) {
    super("Mesh Emitter Example", false, assetManager);
  }

  @Override
  public void addEmitterToNode(Node rootNode) {
    Material mat = new Material(assetManager,
        "Common/MatDefs/Light/Lighting.j3md");
    mat.setColor("Diffuse", ColorRGBA.Blue);
    Torus torus = new Torus(20, 20, .5f, 1.5f);
    Geometry model = new Geometry("emitMesh", torus);
    model.setMaterial(mat);
    emitter = new Emitter("test", getPartMat("Effects/Particles/part_light.png"), 100,
        new ColorInfluencer(),
        new SizeInfluencer()
    );

    emitter.getInfluencer(ColorInfluencer.class).setColorOverTime(new ColorValueType(
        new Gradient().addGradPoint(new ColorRGBA(0.8f, 0.6f, 0.0f, 0.6f), 0.0f)
            .addGradPoint(new ColorRGBA(0.8f, 0.0f, 0.0f, 0.5f), 0.8f)
            .addGradPoint(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.0f), 1.0f)
    ));


    emitter.getInfluencer(SizeInfluencer.class).setSizeOverTime(new ValueType(new Curve()
        .addControlPoint(null, new Vector2f(0.0f, 1.0f), new Vector2f(0.3f, 1.0f))
        .addControlPoint(new Vector2f(0.7f, 0.5f), new Vector2f(1.0f, 0.5f), null)
    ));

    emitter.setStartSize(new ValueType(0.1f));
    emitter.setStartSpeed(new ValueType(0.1f));
    emitter.setLifeMinMax(new ValueType(1.0f), new ValueType(1.0f));
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(4);
    emitter.setShape(new EmitterMesh(((Geometry)model).getMesh()));

    model.rotate(0, FastMath.PI, 0);
    model.scale(3);
    emitter.scale(3);
    rootNode.attachChild(model);
    rootNode.attachChild(emitter);
  }
}
