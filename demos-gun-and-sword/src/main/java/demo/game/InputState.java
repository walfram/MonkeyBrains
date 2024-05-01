package demo.game;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

public class InputState extends BaseAppState {

  private static final FunctionId FUNC_MOVE_LEFT = new FunctionId("move-left");
  private static final FunctionId FUNC_MOVE_RIGHT = new FunctionId("move-right");
  private static final FunctionId FUNC_MOVE_FORWARD = new FunctionId("move-forward");
  private static final FunctionId FUNC_MOVE_BACKWARD = new FunctionId("move-backward");
  
  @Override
  protected void initialize(Application app) {
    InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
    
    inputMapper.map(FUNC_MOVE_LEFT, KeyInput.KEY_A);
    inputMapper.map(FUNC_MOVE_RIGHT, KeyInput.KEY_D);
    inputMapper.addAnalogListener(this::strafe, FUNC_MOVE_LEFT, FUNC_MOVE_RIGHT);
    
    inputMapper.map(FUNC_MOVE_FORWARD, KeyInput.KEY_W);
    inputMapper.map(FUNC_MOVE_BACKWARD, KeyInput.KEY_S);
    inputMapper.addAnalogListener(this::move, FUNC_MOVE_FORWARD, FUNC_MOVE_BACKWARD);
  }

  private void move(FunctionId func, double value, double tpf) {
    double d = func.equals(FUNC_MOVE_BACKWARD) ? -1 : 1; 
    getState(PlayerState.class).move(d * value, tpf);
  }

  private void strafe(FunctionId func, double value, double tpf) {
    double d = func.equals(FUNC_MOVE_RIGHT) ? -1 : 1;
    getState(PlayerState.class).strafe(d * value, tpf);
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
