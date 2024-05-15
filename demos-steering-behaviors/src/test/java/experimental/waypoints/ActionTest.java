package experimental.waypoints;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ActionTest {

  private static final Logger logger = LoggerFactory.getLogger(ActionTest.class);
  
  private final NpcMoveContext context = new NpcMoveContext(List.of(
      Vector3f.ZERO, Vector3f.UNIT_X, Vector3f.UNIT_Z
  ), Vector3f.ZERO);
  
  private Node subject;
  
  @BeforeEach
  void setup() {
    AnimComposer control = new AnimComposer();
    control.addAnimClip(new AnimClip("Run"));
    
    subject = new Node("subject");
    subject.addControl(control);
  }
  
  @Test
  void idle_action_should_return_plan_action() {
    IdleAction source = new IdleAction(context);
    
    source.update(5f);
    
    assertTrue(source.isDone());
    
    Action<NpcMoveContext> action = source.nextAction(context, subject);
    assertInstanceOf(PlanAction.class, action);
  }

  @Test
  void plan_action_should_return_move_action() {
    PlanAction source = new PlanAction(context);
    source.update(1f);

    Vector3f initialTranslation = subject.getWorldTranslation().clone();
    logger.debug("initial subject translation = {}", initialTranslation);

    Action<NpcMoveContext> action = source.nextAction(context, subject);
    assertInstanceOf(MoveAction.class, action);

    action.update(0.5f);

    Vector3f finalTranslation = subject.getWorldTranslation().clone();
    logger.debug("final subject translation = {}", finalTranslation);

    // TODO check if action moved npc...
    assertNotEquals(initialTranslation, finalTranslation);
  }
  
}
