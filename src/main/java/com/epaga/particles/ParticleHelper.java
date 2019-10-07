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
package com.epaga.particles;

import com.jme3.anim.tween.AbstractTween;
import com.jme3.anim.tween.Tween;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.epaga.particles.influencers.GravityInfluencer;
import com.epaga.particles.influencers.SizeInfluencer;
import com.epaga.particles.valuetypes.ColorValueType;
import com.epaga.particles.valuetypes.Curve;
import com.epaga.particles.valuetypes.ValueType;
import com.epaga.particles.valuetypes.VectorValueType;

/**
 * Particle Helper
 *
 * This is a helper class
 *
 * @author Jedic
 */
public class ParticleHelper {

  public static void minMaxSize(Emitter e, float min, float max) {
    e.setStartSize(new ValueType(1));
    Curve curve = new Curve();
    curve.addControlPoint(null, new Vector2f(0.0f, min), new Vector2f(0.25f, min));
    curve.addControlPoint(new Vector2f(0.75f, max), new Vector2f(0.75f, max), null);
    e.getInfluencer(SizeInfluencer.class).setSizeOverTime(new ValueType(curve));
  }

  public static Tween startSize(Emitter emitter, double length, float startSize, float endSize) {
    return new ValueTween(length, emitter.getStartSize(), startSize, endSize);
  }

  public static Tween lifeMin(Emitter emitter, double length, float min, float max) {
    return new ValueTween(length, emitter.getLifeMin(), min, max);
  }

  public static Tween lifeMax(Emitter emitter, double length, float min, float max) {
    return new ValueTween(length, emitter.getLifeMax(), min, max);
  }

  public static Tween startSpeed(Emitter emitter, double length, float min, float max) {
    return new ValueTween(length, emitter.getStartSpeed(), min, max);
  }

  public static Tween gravity(Emitter emitter, double length, Vector3f min, Vector3f max) {
    GravityInfluencer gravityInfluencer = emitter.getInfluencer(GravityInfluencer.class);
    return new VectorValueTween(length, min, max, gravityInfluencer.getGravity());
  }

  public static Tween startColor(Emitter emitter, double length, ColorRGBA min, ColorRGBA max) {
    return new ColorValueTween(length, min, max,emitter.getStartColor());
  }

  public static Tween emissions(Emitter emitter, double length, int min, int max) {
    return new AbstractTween(length){
      @Override
      protected void doInterpolate(double t) {
        emitter.setEmissionsPerSecond((int) (min + (max - min) * t));
      }
    };
  }

  private static class ValueTween extends AbstractTween {
    private float min;
    private float max;
    private ValueType typeTarget;

    public ValueTween(double length, ValueType target, float min, float max) {
      super(length);
      this.min = min;
      this.max = max;
      this.typeTarget = target;
    }

    @Override
    protected void doInterpolate(double t) {
      typeTarget.setValue((float) (min + (max - min) * t));
    }
  }

  private static class VectorValueTween extends AbstractTween {
    private Vector3f min;
    private Vector3f max;
    private VectorValueType typeTarget;
    private Vector3f temp = new Vector3f();

    public VectorValueTween(double length, Vector3f min, Vector3f max, VectorValueType typeTarget) {
      super(length);
      this.min = min;
      this.max = max;
      this.typeTarget = typeTarget;
    }

    @Override
    protected void doInterpolate(double t) {
      temp.x = (float)(min.x + (max.x - min.x)*t);
      temp.y = (float)(min.y + (max.y - min.y)*t);
      temp.z = (float)(min.z + (max.z - min.z)*t);
      typeTarget.setValue(temp);
    }
  }


  private static class ColorValueTween extends AbstractTween {
    private ColorRGBA min;
    private ColorRGBA max;
    private ColorValueType typeTarget;
    private ColorRGBA temp = new ColorRGBA();

    public ColorValueTween(double length, ColorRGBA min, ColorRGBA max, ColorValueType typeTarget) {
      super(length);
      this.min = min;
      this.max = max;
      this.typeTarget = typeTarget;
    }

    @Override
    protected void doInterpolate(double t) {
      temp.r = (float)(min.r + (max.r - min.r)*t);
      temp.g = (float)(min.g + (max.g - min.g)*t);
      temp.b = (float)(min.b + (max.b - min.b)*t);
      temp.a = (float)(min.a + (max.a - min.a)*t);
      typeTarget.setColor(temp);
    }
  }

}
