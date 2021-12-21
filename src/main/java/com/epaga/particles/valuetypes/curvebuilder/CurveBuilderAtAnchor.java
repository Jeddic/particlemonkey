package com.epaga.particles.valuetypes.curvebuilder;

import com.epaga.particles.valuetypes.Curve;
import com.jme3.math.Vector2f;

public class CurveBuilderAtAnchor extends CurveBuilderPiece{

  private final Curve curveBeingBuilt;
  private final Vector2f controlPointIn;
  private final Vector2f currentAnchor;

  public CurveBuilderAtAnchor(Curve curveBeingBuilt, Vector2f controlPointIn, Vector2f currentAnchor){
    this.curveBeingBuilt = curveBeingBuilt;
    this.controlPointIn = controlPointIn;
    this.currentAnchor = currentAnchor;
  }

  /**
   * Adds a point that the curve will attempt to move towards (but may not actually touch).
   *
   * The 2 control points are used to define a cubic Bézier-like curve between 2 anchors
   * @param x the next control point's x
   * @param y the next control point's y
   * @return a CurveBuilderAtControlPoint1 a part of the curve builder system
   */
  public CurveBuilderAtControlPoint1 controlPoint1( float x, float y ){
    return controlPoint1(new Vector2f(x,y));
  }

  /**
   * Adds a point that the curve will attempt to move towards (but may not actually touch)
   *
   * The 2 control points are used to define a cubic Bézier-like curve between 2 anchors
   * @param nextControlPoint the control point
   * @return a CurveBuilderAtControlPoint1 a part of the curve builder system
   */
  public CurveBuilderAtControlPoint1 controlPoint1( Vector2f nextControlPoint ){
    checkReuse();
    return new CurveBuilderAtControlPoint1(curveBeingBuilt, controlPointIn, currentAnchor, nextControlPoint);
  }

  /**
   * Produces a straight line between 2 anchor points
   * @param x the x of the next anchor point
   * @param y the y of the next anchor point
   * @return a CurveBuilderAtAnchor a part of the curve builder system
   */
  public CurveBuilderAtAnchor anchorPoint(float x, float y){
    return anchorPoint(new Vector2f(x,y));
  }

  /**
   * Produces a straight line between 2 anchor points
   * @param nextAnchor the next anchor point
   * @return a CurveBuilderAtAnchor a part of the curve builder system
   */
  public CurveBuilderAtAnchor anchorPoint(Vector2f nextAnchor ){
    //no checkReuse() as the call to controlPoint1 will do that
    //simulate a straight line using a Bézier-like curve
    Vector2f midOne = currentAnchor.mult(2f/3).add(nextAnchor.mult(1f/3));
    Vector2f midTwo = currentAnchor.mult(1f/3).add(nextAnchor.mult(2f/3));
    return controlPoint1(midOne).controlPoint2(midTwo).anchorPoint(nextAnchor);
  }

  public Curve build(){
    checkReuse();
    curveBeingBuilt.addControlPoint(controlPointIn, currentAnchor, null);
    return curveBeingBuilt;
  }

}