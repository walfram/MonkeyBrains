//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jme3.ai.agents.behaviours.npc;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviours.Behaviour;
import com.jme3.ai.agents.events.GameObjectSeenEvent;
import com.jme3.ai.agents.events.GameObjectSeenListener;
import com.jme3.ai.agents.util.GameObject;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;

public class SimpleAttackBehaviour extends Behaviour implements GameObjectSeenListener {
  protected GameObject targetedObject;
  protected Vector3f targetPosition;

  public SimpleAttackBehaviour(Agent agent) {
    super(agent);
  }

  public SimpleAttackBehaviour(Agent agent, Spatial spatial) {
    super(agent, spatial);
  }

  public void setTarget(GameObject target) {
    this.targetedObject = target;
  }

  public void setTarget(Vector3f target) {
    this.targetPosition = target;
  }

  protected void controlUpdate(float tpf) {
    if (this.agent.getWeapon() != null) {
      if (this.targetPosition != null) {
        this.agent.getWeapon().attack(this.targetPosition, tpf);
        this.targetPosition = null;
      } else if (this.targetedObject != null && this.targetedObject.isEnabled()) {
        this.agent.getWeapon().attack(this.targetedObject, tpf);
      }
    }

  }

  protected void controlRender(RenderManager rm, ViewPort vp) {
  }

  public void handleGameObjectSeenEvent(GameObjectSeenEvent event) {
    if (event.getGameObjectSeen() instanceof Agent) {
      Agent targetAgent = (Agent)event.getGameObjectSeen();
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

  public boolean isTargetSet() {
    return this.targetPosition != null && this.targetedObject != null;
  }
}
