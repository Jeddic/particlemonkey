# Particle Monkey
This is a particle emitter implementation based on some early code from t0neg0d's particle emitter and extended to allow for particle attribute animation. 


**Gradle**
-
``` groovy
dependencies {
    compile 'com.epaga:particlemonkey:1.0.1'
}
```

**Basic Usage**
-
``` java
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
    Texture tex = assetManager.loadTexture("Effects/Particles/part_light.png");
    mat.setTexture("Texture", tex);
    
    Emitter emitter = new Emitter("test", mat, 100);
    emitter.setStartSpeed(new ValueType(6.5f));
    emitter.setLifeFixedDuration(2.0f);
    emitter.setEmissionsPerSecond(20);
    emitter.setParticlesPerEmission(1);
    emitter.setShape(new EmitterCone());
    ((EmitterCone)emitter.getShape()).setRadius(0.005f);

    emitter.setLocalTranslation(0, 0.5f, 0);
    rootNode.attachChild(emitter);
```

For further usage please visit the [Wiki](https://github.com/Jeddic/particlemonkey/wiki)
