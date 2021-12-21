package com.epaga.particles.valuetypes;

import com.epaga.particles.valuetypes.curvebuilder.CurveBuilderAtAnchor;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurveTest{

  @Test
  public void builder_straightLine(){
    Curve curve = Curve.builder()
        .anchorPoint(0,0)
        .anchorPoint(1,10)
        .build();

    assertEquals(0, curve.getValue(0f), 0.001);
    assertEquals(4, curve.getValue(0.4f), 0.001);
    assertEquals(10, curve.getValue(1f), 0.001);
  }

  /**
   * Tests that 2 straight lines joined together functions correctly
   */
  @Test
  public void builder_doubleStraightLine(){
    Curve curve = Curve.builder()
        .anchorPoint(0,0)
        .anchorPoint(0.4f,10)
        .anchorPoint(1f, 10)
        .build();

    assertEquals(0, curve.getValue(0f), 0.001);
    assertEquals(5, curve.getValue(0.2f), 0.001);
    assertEquals(10, curve.getValue(0.8f), 0.001);
  }

  /**
   * Tests that a Bézier-like curve functions correctly
   *
   * (Its not actually a true Bézier curve becuse a Bézier curve can "go backwards" and follows a
   * slightly different path
   */
  @Test
  public void builder_curve(){

    Curve curve = Curve.builder()
        .anchorPoint(0,0)
        .controlPoint1(0.2f, 1)
        .controlPoint2(0.8f, 0)
        .anchorPoint(1,1)
        .build();

    //expected values obtained using https://www.desmos.com/calculator/ebdtbxgbq0

    assertEquals(0, curve.getValue(0f), 0.001);

    //value obtained as 0.1 along using the following
    // along line 1 = 0.9 * 0 + 0.1 * 1 = 0.1
    // along line 2 = 0.9 * 1 + 0.1 * 0 = 0.9
    // along line 3 = 0.9 * 0 + 0.1 * 1 = 0.1

    //obtain 2 new lines between along line 1  -> along line 2 and along line 2 -> along line 3. Get 0.1 along each one
    //along second order 1 = 0.9 * 0.1 + 0.1 * 0.9 = 0.18
    //along second order 2 = 0.9 * 0.9 + 0.1 * 0.1 = 0.82

    //final result is 0.1 along the line between the second order points
    // 0.9 * 0.18 + 0.1 * 0.82

    assertEquals(0.244, curve.getValue(0.1f), 0.001);

    assertEquals(0.5, curve.getValue(0.5f), 0.001);
    assertEquals(1, curve.getValue(1), 0.001);
  }

  @Test(expected = IllegalStateException.class)
  public void builder_reuseLeadsToException(){
    CurveBuilderAtAnchor builder = Curve.builder()
        .anchorPoint(0,0);

    Curve legalUse = builder.build();
    Curve illegalReuse = builder.build();
  }

}
