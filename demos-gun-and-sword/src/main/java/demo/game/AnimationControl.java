package demo.game;

import com.jme3.anim.AnimComposer;
import jme3utilities.SimpleControl;

public class AnimationControl extends SimpleControl {

  private final AnimComposer animComposer;

  public AnimationControl(AnimComposer animComposer) {
    this.animComposer = animComposer;
  }

  // "base_stand", "run_01", "shoot", "strike_sword"
  
  public void stand() {
    animComposer.setCurrentAction("base_stand");
  }
  
  public void run() {
    animComposer.setCurrentAction("run_01");
  }
  
  public void shoot() {
    animComposer.setCurrentAction("shoot");
  }
  
  public void strike() {
    animComposer.setCurrentAction("strike_sword");
  }
  
}
