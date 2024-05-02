package demo.ai;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.steering.SeekBehavior;
import com.jme3.ai.agents.events.AIControlSeenEvent;
import com.jme3.ai.agents.events.AIControlSeenListener;
import demo.model.Model;

public class AISeekBehavior extends SeekBehavior implements AIControlSeenListener {

  @Override
  public void handleAIControlSeenEvent(AIControlSeenEvent event) {
    if (event.getAIControlSeen() instanceof Agent targetAgent) {
      Model model = (Model) targetAgent.getModel();

      if (model.isSameTeam(targetAgent)) {
        return;
      }
      
      setTarget(targetAgent);
    }
  }
}
