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
    Spatial player = app.getAssetManager().loadModel("Models/Demo_01/characters/character_01/character_01.j3o");
    // TODO check method setGraphicModel
    scene.attachChild(player);

    Node armature = (Node) ((Node) player).getChild("Armature");
    Node characterMan = (Node) armature.getChild("characterMan");
    Node characterManEntity = (Node) characterMan.getChild("characterMan-entity");
    Node entity = (Node) characterManEntity.getChild("characterMan-ogremesh");
    AnimMigrationUtils.migrate(entity);
    entity.getControl(AnimComposer.class).setCurrentAction("base_stand");
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
