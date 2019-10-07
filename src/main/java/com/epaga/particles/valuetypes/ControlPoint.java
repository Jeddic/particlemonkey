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
import com.jme3.math.Vector2f;

import java.io.IOException;

public class ControlPoint implements Savable, Cloneable {
  public Vector2f inControlPoint;
  public Vector2f point;
  public Vector2f outControlPoint;

  public ControlPoint() {

  }

  public ControlPoint(Vector2f in, Vector2f p, Vector2f o) {
    this.inControlPoint = in;
    this.point = p;
    this.outControlPoint = o;
  }

  @Override
  public void write(JmeExporter ex) throws IOException {
    OutputCapsule oc = ex.getCapsule(this);
    oc.write(inControlPoint, "incontrolpoint", new Vector2f());
    oc.write(point, "point", new Vector2f());
    oc.write(outControlPoint, "outcontrolpoint", new Vector2f());
  }

  @Override
  public void read(JmeImporter im) throws IOException {
    InputCapsule ic = im.getCapsule(this);
    inControlPoint = (Vector2f)ic.readSavable("incontrolpoint", new Vector2f());
    point = (Vector2f)ic.readSavable("point", new Vector2f());
    outControlPoint = (Vector2f)ic.readSavable("outcontrolpoint", new Vector2f());
  }

  @Override
  public ControlPoint clone() {
    try {
      ControlPoint clone = (ControlPoint)super.clone();
      if (inControlPoint != null) clone.inControlPoint = inControlPoint.clone();
      clone.point = point.clone();
      if (outControlPoint != null ) clone.outControlPoint = outControlPoint.clone();
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public boolean equals(Object o) {
    if (!(o instanceof ControlPoint)) return false;

    ControlPoint check = (ControlPoint)o;

    if (inControlPoint != null && !inControlPoint.equals(check.inControlPoint)
      || inControlPoint == null && check.inControlPoint != null) {
      return false;
    }

    if (point != null && !point.equals(check.point)
        || point == null && check.point != null) {
      return false;
    }

    if (outControlPoint != null && !outControlPoint.equals(check.outControlPoint)
        || outControlPoint == null && check.outControlPoint != null) {
      return false;
    }

    return true;
  }
}
