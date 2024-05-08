package demo.model;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;

public class Weapons {
  
  private final Weapon sword = new Weapon(1f);
  private final Weapon gun = new Weapon(100f);

  public Weapon gun() {
    return gun;
  }
  
  public Weapon sword() {
    return sword;
  }

  public void attack(Vector3f targetPosition, float tpf) {
    
  }

  public void attack(AIControl targetedObject, float tpf) {
    
  }

  public boolean isInRange(Agent targetAgent) {
    return false;
  }

  public boolean isCoolDown() {
    return gun.isCoolDown();
  }

  public void startCoolDown() {
    gun.startCoolDown();
  }

  public void update(float tpf) {
    gun.update(tpf);
    sword.update(tpf);
  }
}
