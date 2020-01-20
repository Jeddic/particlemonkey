/*
 * Copyright (c) 2019 Greg Hoffman
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.epaga;

import com.epaga.particles.BillboardMode;
import com.epaga.particles.Emitter;
import com.epaga.particles.ParticleHelper;
import com.epaga.particles.emittershapes.*;
import com.epaga.particles.examples.*;
import com.epaga.particles.influencers.*;
import com.epaga.particles.particle.ParticleDataPointMesh;
import com.epaga.particles.particle.ParticleDataTemplateMesh;
import com.epaga.particles.valuetypes.*;
import com.jme3.anim.tween.Tween;
import com.jme3.app.LostFocusBehavior;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector2f;
import com.jme3.app.SimpleApplication;

import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.*;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.util.TangentBinormalGenerator;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main extends SimpleApplication {

  public static void main(String[] args){
    Main app = new Main();
    app.setLostFocusBehavior(LostFocusBehavior.Disabled);
    app.start();
  }

  private Frame editorWindow;
  private Geometry floor;
  private BitmapText hudText;
  private Emitter emitter;
  private Tween emitterTween;
  private double tweenAmount = 0.0f;
  private boolean debug = false;
  private FilterPostProcessor filterPostProcessor = null;
  private TranslucentBucketFilter translucentFilter;
  private List<ParticleExample> exampleList = new LinkedList<>();

  public void addFloor() {
    if (floor != null) {
      rootNode.attachChild(floor);
    } else {
      Box b = new Box(8, 0.01f, 8);
      floor = new Geometry("Box", b);
      TangentBinormalGenerator.generate(b);
      Material mat = new Material(assetManager,
          "Common/MatDefs/Light/Lighting.j3md");
      mat.setTexture("DiffuseMap",
          assetManager.loadTexture("Effects/Particles/grid.png"));
      floor.setMaterial(mat);
      floor.setLocalTranslation(0, 0, 0);
      rootNode.attachChild(floor);


      SpotLight spot = new SpotLight();
      spot.setSpotRange(100f);                           // distance
      spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
      spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
      spot.setColor(ColorRGBA.White.mult(1.3f));         // light color
      spot.setPosition(new Vector3f(0, 10, 0));               // shine from camera loc
      spot.setDirection(new Vector3f(0, -1, 0));             // shine forward from camera loc
      spot.setColor(new ColorRGBA(0.6f, 0.4f, 0.95f, 1.0f));
      rootNode.addLight(spot);

      DirectionalLight sun = new DirectionalLight();
      sun.setDirection(new Vector3f(1, -1, -2).normalizeLocal());
      sun.setColor(ColorRGBA.White);
      rootNode.addLight(sun);
    }
  }

  public Frame getEditorWindow() {
    return editorWindow;
  }

  public void setEditorWindow(Frame editorWindow) {
    this.editorWindow = editorWindow;
  }

  private int currentTest = 0;

  private String buildDisplayString() {
    StringBuilder builder = new StringBuilder();

    builder.append("Current Test: ");

    ParticleExample example = exampleList.get(currentTest);
    builder.append(example.getName());

    builder.append("\n\nControls:\n");
    builder.append("1 - Previous Test\n");
    builder.append("2 - Next Test\n");
    builder.append("3 - Enable/Disable Debug\n");

    if (example.hasTweenCtrl()) {
      builder.append("4 - Increase Tween Value\n");
      builder.append("5 - Decrease Tween Value\n");
      builder.append("Tween Amount: ");
      builder.append(tweenAmount);
    }

    return builder.toString();
  }

  private void setTest() {
    rootNode.detachAllChildren();
    addFloor();
    tweenAmount = 0.0f;
    emitterTween = null;


    if (currentTest < 0) currentTest = exampleList.size() - 1;
    if (currentTest >= exampleList.size()) currentTest = 0;

    tweenAmount = 0.0f;
    ParticleExample example = exampleList.get(currentTest);
    example.setCollisionShape(floor);
    example.addEmitterToNode(rootNode);
    emitter = example.getEmitter();
    emitterTween = example.getEmitterTween();

    if (debug) {
      emitter.setDebug(assetManager, debug, debug);
    }

    hudText.setText(buildDisplayString());
  }

  @Override
  public void stop(boolean waitFor) {
    super.stop(waitFor);

    if (editorWindow != null) {
      editorWindow.dispose();
    }
  }

  @Override
  public void simpleInitApp() {
    filterPostProcessor = new FilterPostProcessor(getAssetManager());
    getViewPort().addProcessor(filterPostProcessor);
    translucentFilter = new TranslucentBucketFilter(true);
    filterPostProcessor.addFilter(translucentFilter);

    hudText = new BitmapText(guiFont, false);
    hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
    hudText.setColor(ColorRGBA.White);                             // font color
    //hudText.setText(buildDisplayString());             // the text
    hudText.setLocalTranslation(10, settings.getHeight() - hudText.getLineHeight(), 0); // position
    guiNode.attachChild(hudText);


    getCamera().setLocation(new Vector3f(10, 10, -10));
    getCamera().lookAt(new Vector3f(), new Vector3f(0, 1, 0));
    getFlyByCamera().setMoveSpeed(15.0f);
    getFlyByCamera().setDragToRotate(true);

    exampleList.add(new SimpleExample(assetManager));
    exampleList.add(new GradientExample(assetManager));
    exampleList.add(new SizeExample(assetManager));
    exampleList.add(new SimplePhysicsExample(assetManager));
    exampleList.add(new TrailExample(assetManager));
    exampleList.add(new VelocityExample(assetManager));
    exampleList.add(new OrbitalVelocityExample(assetManager));
    exampleList.add(new DestinationExample(assetManager));
    exampleList.add(new SpriteExample(assetManager));
    exampleList.add(new MeshEmitExample(assetManager));
    exampleList.add(new TweenExample(assetManager));
    exampleList.add(new TimeOrbExample(assetManager));

    setTest();

    inputManager.addListener(new ActionListener() {

      public void onAction(String name, boolean isPressed, float tpf) {
        if ("next".equals(name) && !isPressed) {
          currentTest += 1;
          setTest();
        }
      }
    }, "next");
    inputManager.addListener(new ActionListener() {

      public void onAction(String name, boolean isPressed, float tpf) {
        if ("prev".equals(name) && !isPressed) {
          currentTest -= 1;
          setTest();
        }
      }
    }, "prev");
    inputManager.addListener(new ActionListener() {

      public void onAction(String name, boolean isPressed, float tpf) {
        if ("debug".equals(name) && !isPressed) {
          debug = !debug;
          emitter.setDebug(assetManager, debug, debug);
        }
      }
    }, "debug");
    inputManager.addListener(new ActionListener() {

      public void onAction(String name, boolean isPressed, float tpf) {
        if ("tweenIncrease".equals(name) && !isPressed && emitterTween != null) {
          tweenAmount += 0.05f;
          if (tweenAmount > 1) tweenAmount = 1;
          emitterTween.interpolate(tweenAmount);
          hudText.setText(buildDisplayString());
        }
      }
    }, "tweenIncrease");
    inputManager.addListener(new ActionListener() {

      public void onAction(String name, boolean isPressed, float tpf) {
        if ("tweenDecrease".equals(name) && !isPressed && emitterTween != null) {
          tweenAmount -= 0.05f;
          if (tweenAmount < 0) tweenAmount = 0.0f;
          emitterTween.interpolate(tweenAmount);
          hudText.setText(buildDisplayString());

        }
      }
    }, "tweenDecrease");
//
    inputManager.addMapping("debug", new KeyTrigger(KeyInput.KEY_3));
    inputManager.addMapping("next", new KeyTrigger(KeyInput.KEY_2));
    inputManager.addMapping("prev", new KeyTrigger(KeyInput.KEY_1));
    inputManager.addMapping("tweenIncrease", new KeyTrigger(KeyInput.KEY_4));
    inputManager.addMapping("tweenDecrease", new KeyTrigger(KeyInput.KEY_5));

  }

}
