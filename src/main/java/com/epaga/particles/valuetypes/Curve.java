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

import com.epaga.particles.valuetypes.curvebuilder.CurveBuilderAtAnchor;
import com.epaga.particles.valuetypes.curvebuilder.CurveBuilderStart;
import com.jme3.export.*;
import com.jme3.math.Vector2f;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Curve implements Savable, Cloneable {

  private LinkedList<ControlPoint> points = new LinkedList<>();

  public Curve() {
  }

  public Curve addControlPoint(Vector2f in, Vector2f point, Vector2f out) {
    points.add(new ControlPoint(in, point, out));
    sort();
    return this;
  }

  private void sort() {
    points.sort((c1, c2)->{
      if (c1.point.x < c2.point.x) return -1;
      else if (c1.point.x > c2.point.x) return 1;
      else return 0;
    });

  }

  public List<ControlPoint> getControlPoints() {
    return points;
  }

  public float getValue(float blendTime) {
    // find which points we are in between
    ControlPoint lastPoint = null;
    ControlPoint currentPoint = null;
    for (int i = 0; i < points.size(); i++) {
      lastPoint = currentPoint;
      currentPoint = points.get(i);

      if (currentPoint.point.x >= blendTime) {
        // now get the interpolated value
        if (lastPoint == null && currentPoint != null) {
          // just use the current points y value
          return currentPoint.point.y;
        } else if (lastPoint != null && currentPoint != null) {
          // Calculate the percent distance we are in between the two points
          float perc = (blendTime - lastPoint.point.x) / (currentPoint.point.x - lastPoint.point.x);

          // get the midpoints of the 3 line segments
          //float p1x = lastPoint.point.x - ((lastPoint.point.x - lastPoint.outControlPoint.x) * perc);
          float p1y = lastPoint.point.y - ((lastPoint.point.y - lastPoint.outControlPoint.y) * perc);

          //float p2x = lastPoint.outControlPoint.x - ((lastPoint.outControlPoint.x - currentPoint.inControlPoint.x) * perc);
          float p2y = lastPoint.outControlPoint.y - ((lastPoint.outControlPoint.y - currentPoint.inControlPoint.y) * perc);

          //float p3x = currentPoint.inControlPoint.x - ((currentPoint.inControlPoint.x - currentPoint.point.x) * perc);
          float p3y = currentPoint.inControlPoint.y - ((currentPoint.inControlPoint.y - currentPoint.point.y) * perc);

          // now get the midpoints of the two segments
          //float s1x = p1x - ((p1x - p2x) * perc);
          float s1y = p1y - ((p1y - p2y) * perc);

          //float s2x = p2x - ((p2x - p3x) * perc);
          float s2y = p2y - ((p2y - p3y) * perc);

          // now get our final value
          //float fx = s1x - ((s1x - s2x) * perc);
          float fy = s1y - ((s1y - s2y) * perc);

          return fy;
        }
      }
    }

    // we must be past the last point?
    if (currentPoint != null && currentPoint.point.x < blendTime) {
      return currentPoint.point.y;
    }


    return 0;
  }

  @Override
  public void write(JmeExporter ex) throws IOException {
    OutputCapsule oc = ex.getCapsule(this);
    ControlPoint[] pointArray = points.toArray(new ControlPoint[points.size()]);
    oc.write(pointArray, "points", new ControlPoint[]{});
  }

  @Override
  public void read(JmeImporter im) throws IOException {
    InputCapsule ic = im.getCapsule(this);
    Savable[] pointArray = (Savable[]) ic.readSavableArray("points", new ControlPoint[]{});
    for (int i = 0; i < pointArray.length; i++) {
      points.add((ControlPoint) pointArray[i]);
    }
  }

  @Override
  public Curve clone() {
    try {
      Curve clone = (Curve)super.clone();
      clone.points = new LinkedList<>();
      points.forEach((p)-> clone.points.add(p.clone()));
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public boolean equals(Object o) {
    if (!(o instanceof Curve)) return false;

    Curve check = (Curve)o;

    if (points.size() != check.points.size()) return false;

    for (int i=0; i < points.size(); i++) {
      if (!points.get(i).equals(check.points.get(i)))
        return false;
    }

    return true;
  }


  /**
   * Produces a builder that can be used to fluently build a curve. A Curve will always be continuous (And should
   * move in a positive X direction) but the gradient may change sharply.
   *
   * It is a series of anchor points connected either by straight line sections or cubic Bézier-like curves (defined by
   * 2 control points). They are bezier-like curves not Bézier curves because of the requirement that X (often
   * representing time) can only be allowed to move forward
   *
   * In normal usage the first anchor point should be at x = 0, all further points should advance in the X axis and
   * the final anchor point should have x at 1. This is because usually X is the fractional life of the particle
   *
   * Example usage:
   *
   * <pre>{@code
   *     Curve curve = Curve.builder()
   *             .anchorPoint(new Vector2f(0,0))
   *             .anchorPoint(new Vector2f(0.5f,0.5f))
   *             .controlPoint1(new Vector2f(0.6f,0.5f))
   *             .controlPoint2(new Vector2f(0.8f,2f))
   *             .anchorPoint(new Vector2f(1,2f))
   *             .build();
   * }</pre>
   *
   * This example produces a straight line from (0,0) to (0.5,0.5), then a cubic Besier curves between (0.5,0.5) to (1,2) with control points (0.6,0.5) and (0.8,2)
   *
   * Note that a builder should not be reused.
   *
   * @return a CurveBuilderStart
   */
  public static CurveBuilderStart builder(){
    return new CurveBuilderStart();
  }
}
