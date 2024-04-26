package demo.game;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(PlayerState.class);
  
  private final Node scene = new Node("scene");
  
  public PlayerState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    Spatial playerSpatial = app.getAssetManager().loadModel("Models/Demo_01/characters/character_01/character_01.j3o");
    // TODO check method setGraphicModel
    scene.attachChild(playerSpatial);

    Spatial armature = ((Node) playerSpatial).getChild("Armature");
    logger.debug("armature controls = {}", armature.getNumControls());
    
    Spatial characterMan = ((Node) armature).getChild("characterMan");
    logger.debug("characterMan controls = {}", characterMan.getNumControls());

    Spatial characterManEntity = ((Node) characterMan).getChild("characterMan-entity");
    logger.debug("characterManEntity controls = {}", characterManEntity.getNumControls());

    Spatial characterManOgreMesh = ((Node) characterManEntity).getChild("characterMan-ogremesh");
    logger.debug("characterManOgreMesh controls = {}", characterManOgreMesh.getNumControls());

    AnimMigrationUtils.migrate(characterManOgreMesh);
    
    AnimComposer composer = characterManOgreMesh.getControl(AnimComposer.class);
    Set<String> animClipsNames = composer.getAnimClipsNames();
    logger.debug("anim clip names = {}", animClipsNames);
    // strike_sword, base_stand, run_01, shoot
    
    composer.setCurrentAction("base_stand");
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
}
