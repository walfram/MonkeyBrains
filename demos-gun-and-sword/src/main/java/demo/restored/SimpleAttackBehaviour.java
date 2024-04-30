package demo.restored;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.ai.agents.events.AIControlSeenEvent;
import com.jme3.ai.agents.events.AIControlSeenListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

public class SimpleAttackBehaviour extends Behavior implements AIControlSeenListener {

  protected AIControl targetedObject;

  public SimpleAttackBehaviour(Agent agent) {
    super();
    setAgent(agent);
  }

  public void setTarget(AIControl target) {
    this.targetedObject = target;
  }

  public boolean isTargetSet() {
    return this.targetedObject != null;
  }

  @Override
  public void updateAI(float tpf) {
    if (this.agent.getWeapon() != null) {
      if (this.targetPosition != null) {
        this.agent.getWeapon().attack(this.targetPosition, tpf);
        this.targetPosition = null;
      } else if (this.targetedObject != null && this.targetedObject.isEnabled()) {
        this.agent.getWeapon().attack(this.targetedObject, tpf);
      }
    }
  }

  @Override
  public void handleAIControlSeenEvent(AIControlSeenEvent event) {
    if (event.getGameObjectSeen() instanceof Agent) {
      Agent targetAgent = (Agent) event.getGameObjectSeen();
      if (this.agent.isSameTeam(targetAgent)) {
        return;
      }

      if (!this.agent.getWeapon().isInRange(targetAgent)) {
        return;
      }
    }

    this.targetedObject = event.getGameObjectSeen();
    this.enabled = true;
  }
}
