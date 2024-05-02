package demo.ai;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.ai.agents.events.AIControlSeenEvent;
import com.jme3.ai.agents.events.AIControlSeenListener;
import com.jme3.math.Vector3f;
import demo.model.Model;

public class SimpleAttackBehaviour extends Behavior implements AIControlSeenListener {

  protected AIControl targetedObject;
  protected Vector3f targetPosition;

  public void setTarget(AIControl target) {
    this.targetedObject = target;
  }
  
  public void setTarget(Vector3f target) {
    this.targetPosition = target;
  }

  public boolean isTargetSet() {
    return this.targetedObject != null;
  }

  @Override
  public void updateAI(float tpf) {
    Model model = (Model) agent.getModel();
    
    if (model.weapons() != null) {
      if (targetPosition != null) {
        model.weapons().attack(targetPosition, tpf);
        targetPosition = null;
      } else if (this.targetedObject != null && this.targetedObject.isEnabled()) {
        model.weapons().attack(targetedObject, tpf);
      }
    }
  }

  @Override
  public void handleAIControlSeenEvent(AIControlSeenEvent event) {
    if (event.getAIControlSeen() instanceof Agent targetAgent) {
      Model targetModel = (Model) targetAgent.getModel();
      
      if (targetModel.isSameTeam(targetAgent)) {
        return;
      }

      Model thisModel = (Model) agent.getModel();
      
      if (!thisModel.weapons().isInRange(targetAgent)) {
        return;
      }
    }

    this.targetedObject = event.getAIControlSeen();
  }
}
