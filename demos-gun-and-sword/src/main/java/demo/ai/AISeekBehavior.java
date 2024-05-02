package demo.ai;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.steering.SeekBehavior;
import com.jme3.ai.agents.events.AIControlSeenEvent;
import com.jme3.ai.agents.events.AIControlSeenListener;
import demo.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AISeekBehavior extends SeekBehavior implements AIControlSeenListener {

  private static final Logger logger = LoggerFactory.getLogger(AISeekBehavior.class);

  @Override
  public void handleAIControlSeenEvent(AIControlSeenEvent event) {
    if (event.getAIControlSeen() instanceof Agent targetAgent) {
      Model model = (Model) targetAgent.getModel();

      if (model.isSameTeam(targetAgent)) {
        return;
      }
      
      logger.debug("{}: setting target {}", agent, targetAgent);
      setTarget(targetAgent);
    }
  }
}
