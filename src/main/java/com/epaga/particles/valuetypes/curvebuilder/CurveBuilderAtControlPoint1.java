package com.epaga.particles.valuetypes.curvebuilder;

import com.epaga.particles.valuetypes.Curve;
import com.jme3.math.Vector2f;

public class CurveBuilderAtControlPoint1 extends CurveBuilderPiece{

  Curve curveBeingBuilt;

  public CurveBuilderAtControlPoint1(Curve curveBeingBuilt, Vector2f controlPointIn, Vector2f currentAnchor, Vector2f controlPointOut){
    this.curveBeingBuilt = curveBeingBuilt;
    this.curveBeingBuilt.addControlPoint(controlPointIn, currentAnchor, controlPointOut);
  }

  /**
   * Adds a point that the curve will attempt to move towards (but may not actually touch).
   *
   * The 2 control points are used to define a cubic Bézier-like curve between 2 anchors
   * @param x the control point's x
   * @param y the control point's y
   * @return a CurveBuilderAtControlPoint1 a part of the curve builder system
   */
  public CurveBuilderAtControlPoint2 controlPoint2( float x, float y ){
    return controlPoint2(new Vector2f(x, y));
  }

  /**
   * Adds a point that the curve will attempt to move towards (but may not actually touch).
   *
   * The 2 control points are used to define a cubic Bézier-like curve between 2 anchors
   * @param nextControlPoint the control point
   * @return a CurveBuilderAtControlPoint1 a part of the curve builder system
   */
  public CurveBuilderAtControlPoint2 controlPoint2( Vector2f nextControlPoint ){
    checkReuse();
    return new CurveBuilderAtControlPoint2(curveBeingBuilt, nextControlPoint);
  }
}
