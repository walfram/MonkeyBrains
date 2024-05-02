package demo.model;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.math.Vector3f;

public class Weapons {
  
  private final Weapon sword = new Weapon();
  private final Weapon gun = new Weapon();

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
}
