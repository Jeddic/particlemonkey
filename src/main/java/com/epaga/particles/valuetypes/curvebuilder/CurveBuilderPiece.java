package com.epaga.particles.valuetypes.curvebuilder;

public class CurveBuilderPiece{

  boolean used = false;

  protected void checkReuse(){
    if (used){
      throw new IllegalStateException("Curve builders must not be reused (As they actually build a single curve as they go along)");
    }
    used = true;
  }
}
