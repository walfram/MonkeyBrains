package demo.game;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import demo.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(GameState.class);

  private final float maxMoveSpeed = 5f;
  private final float rotationSpeed = FastMath.DEG_TO_RAD * 90f;
  
  @Override
  protected void initialize(Application app) {
  }

  @Override
  protected void cleanup(Application app) {
  }

  @Override
  protected void onEnable() {
  }

  @Override
  protected void onDisable() {
  }

  public void createNpcAgent(Spatial spatial) {
    Agent<Model> agent = new Agent<>(1f);
    agent.setModel(new Model());
    agent.setMaxMoveSpeed(maxMoveSpeed);
    agent.setRotationSpeed(rotationSpeed);

    SimpleMainBehavior main = new SimpleMainBehavior();

    WanderAreaBehavior wander = new WanderAreaBehavior();
    wander.setArea(spatial.getWorldTranslation(), new Vector3f(2, 0, 2));
    main.addBehavior(wander);

//        SeekBehavior seek = new SeekBehavior(agent)

    agent.setMainBehavior(main);
    spatial.addControl(agent);
    
    logger.debug("agent = {}", agent);
  }

  public void createPlayerAgent(Spatial player) {
    Agent<Model> agent = new Agent<>(1f);
    agent.setModel(new Model());
    agent.setMaxMoveSpeed(maxMoveSpeed);
    agent.setRotationSpeed(rotationSpeed);
    
    // TODO add some behavior to player agent
    
    player.addControl(agent);
    
    // TODO notify npc agents about player
  }
}
