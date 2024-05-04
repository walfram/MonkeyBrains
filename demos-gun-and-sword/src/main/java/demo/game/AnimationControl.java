package demo.game;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
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
    Action shoot = animComposer.action("shoot");
    Tween standTween = Tweens.callMethod(this, "stand");
    animComposer.actionSequence("shootOnce", shoot, standTween);
    animComposer.setCurrentAction("shootOnce");
  }
  
  public void strike() {
    Action strike = animComposer.action("strike_sword");
    Tween standTween = Tweens.callMethod(this, "stand");
    animComposer.actionSequence("strikeOnce", strike, standTween);
    animComposer.setCurrentAction("strikeOnce");
  }

}
