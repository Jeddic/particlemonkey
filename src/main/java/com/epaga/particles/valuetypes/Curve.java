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
import java.util.LinkedList;
import java.util.List;

/**
 * The curve represents a continous function that parses through a number of control
 * points. See {@link Curve#addControlPoint} for details on how these control points work
 */
public class Curve implements Savable, Cloneable {

  private LinkedList<ControlPoint> points = new LinkedList<>();

  public Curve() {
  }

  /**
   * Adds a control point to the curve. The line will enter the control point with the following
   * rules but may curve between control points so a continuous line is formed.
   *
   * Note that if the implied gradient entering the control point is different from the implied gradient exiting it then
   * the gradient may sharply change (but the line itself will remain continuous).
   *
   * Between each pair of control points acts as an independent cubic Bézier-like curve defined by the following:
   *
   * anchor 1 - the 'point' of the first control point (the line will pass through this point)
   * intermediate point 1 - the 'out' of the first control point (the line may not go through this point but it controls the initial direction the line exits anchor 1)
   * intermediate point 2 - the 'in' of the second control point (the line may not go through this point but it controls the initial direction the line enters anchor 2)
   * anchor 2 - the 'point' of the second control point (the line will pass through this point)
   *
   * Instinctively you can think of each section "trying" to go from anchor 1 to intermediate point 1 to intermediate point 2 to anchor 2
   * but being smoothed using a quadratic curve such that it may not actually touch the intermediate points.
   *
   * Note; if this is the first control point the in doesn't matter and if its the last control point the out doesn't matter
   * 
   * Note; these are Bézier-like curve but not Bézier curves due to the requirement that X must always move forwards
   * (as it often represents time)
   *
   * @param in
   *          the intermediate point immediately before this anchor point.
   *          This is a control point in Bézier curve terminology
   * @param point
   *          the line will pass through this point. This is an anchor point in Bézier curve terminology
   * @param out
   *          the intermediate point immediately after this anchor point.
   *          This is a control point in Bézier curve terminology
   * @return this curve
   */
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
          //what y should be according to the the line from lastPoint.point to lastPoint.outControlPoint
          float p1y = lastPoint.point.y - ((lastPoint.point.y - lastPoint.outControlPoint.y) * perc);

          //float p2x = lastPoint.outControlPoint.x - ((lastPoint.outControlPoint.x - currentPoint.inControlPoint.x) * perc);
          //what y should be according to the the line from lastPoint.outControlPoint to currentPoint.inControlPoint
          float p2y = lastPoint.outControlPoint.y - ((lastPoint.outControlPoint.y - currentPoint.inControlPoint.y) * perc);

          //float p3x = currentPoint.inControlPoint.x - ((currentPoint.inControlPoint.x - currentPoint.point.x) * perc);
          //what y should be according to the the line from lastPoint.outControlPoint to currentPoint.inControlPoint
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
}
