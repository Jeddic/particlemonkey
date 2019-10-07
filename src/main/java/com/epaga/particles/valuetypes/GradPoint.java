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
package com.epaga.particles.valuetypes;

import com.jme3.export.*;
import com.jme3.math.ColorRGBA;

import java.io.IOException;

/**
 * Grad Point
 * This represents a gradient point entry for use in the gradient value type
 *
 * @author Jedic
 */
public class GradPoint implements Savable, Cloneable {
  public ColorRGBA color;
  public float x;

  public GradPoint(ColorRGBA color, float x) {
    this.color = color;
    this.x = x;
  }

  @Override
  public void write(JmeExporter ex) throws IOException {
    OutputCapsule oc = ex.getCapsule(this);
    oc.write(x, "x", 0.0f);
    oc.write(color, "color", ColorRGBA.White);
  }

  @Override
  public void read(JmeImporter im) throws IOException {
    InputCapsule ic = im.getCapsule(this);
    x = ic.readFloat("x", 0.0f);
    color = (ColorRGBA) ic.readSavable("color", new ColorRGBA());
  }

  @Override
  public GradPoint clone() {
    try {
      GradPoint p = (GradPoint)super.clone();
      p.x = x;
      p.color = color.clone();
      return p;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }


  public boolean equals(Object o) {
    if (!(o instanceof GradPoint)) return false;

    GradPoint check = (GradPoint)o;

    if (x != check.x) return false;

    if (color != null && !color.equals(check.color)
        || color == null && check.color != null) {
      return false;
    }

    return true;
  }
}