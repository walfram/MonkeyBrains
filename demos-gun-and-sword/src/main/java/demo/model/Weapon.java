package demo.model;

import com.jme3.ai.agents.AIControl;

public class Weapon {
  
  private final float range;
  
  private final float coolDownTimeout = 5f;
  private float elapsedCoolDown = 0f;
  private boolean coolingDown = false;

  public Weapon(float range) {
    this.range = range;
  }

  public void attack(AIControl target, float tpf) {
  }

  public boolean isInRange(AIControl owner, AIControl targetedObject) {
    return owner.getWorldTranslation().distance(targetedObject.getWorldTranslation()) <= range;
  }

  public boolean isCoolDown() {
    return coolingDown;
  }

  public void startCoolDown() {
    coolingDown = true;
    elapsedCoolDown = 0f;
  }

  public void update(float tpf) {
    if (coolingDown) {
      elapsedCoolDown += tpf;
    }
    
    if (elapsedCoolDown >= coolDownTimeout) {
      coolingDown = false;
      elapsedCoolDown = 0f;
    }
      
  }
}
