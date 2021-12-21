package com.epaga.particles.valuetypes.curvebuilder;

import com.epaga.particles.valuetypes.Curve;
import com.jme3.math.Vector2f;

public class CurveBuilderAtControlPoint2 extends CurveBuilderPiece{

  Curve curveBeingBuilt;
  Vector2f inControlPoint;

  public CurveBuilderAtControlPoint2(Curve curveBeingBuilt, Vector2f inControlPoint){
    this.curveBeingBuilt = curveBeingBuilt;
    this.inControlPoint = inControlPoint;
  }

  /**
   * Adds a point that the curve go through.
   *
   * Anchors are the starts and ends of cubic Bézier-like curves
   * @param x the anchor point's x
   * @param y the anchor point's y
   * @return a CurveBuilderAtAnchor a part of the curve builder system
   */
  public CurveBuilderAtAnchor anchorPoint(float x, float y){
    return anchorPoint(new Vector2f(x, y));
  }

  /**
   * Adds a point that the curve will go through.
   *
   * Anchors are the starts and ends of cubic Bézier-like curves
   * @param nextAnchor the anchor point
   * @return a CurveBuilderAtAnchor a part of the curve builder system
   */
  public CurveBuilderAtAnchor anchorPoint(Vector2f nextAnchor ){
    checkReuse();
    return new CurveBuilderAtAnchor(curveBeingBuilt, inControlPoint, nextAnchor);
  }
}
