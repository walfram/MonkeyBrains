package com.jme3.ai.agents;

/**
 * The ApplyType is of interest as soon as you want to do more than the simple
 * Spatial movement.<br>
 * It defines in what way the results of the AI control are <b>applied</b> to
 * the Spatial.<br>
 * 
 * @author MeFisto94
 */
public enum ApplyType {
    /**
     * Spatial is the legacy/default way of applying changes.<br>
     * Here we simply set the Spatials worldTranslation and worldRotation.
     */
    Spatial,
    
    /**
     * When working with BulletPhysics you might notice that changing the
     * position is overwritten by the Physics each tick.<br>
     * You need <code>setPhysicsLocation</code> instead. This mode will do this.
     * <br>Prepare for wierd physics bugs though when two physics objects
     * overlap. As such you might rather want the DontApply Mode and manually
     * check for colissions first.
     */
    RigidBody,
    
    /**
     * Just like the RigidBody, this mode will take care of the specifics for
     * the BetterCharacterControl (Setting it's view and walkDirection).<br>
     */
    BetterCharacterControl,
    
    /**
     * This is the mode for manual application. When using this mode, you can 
     * simply grab {@link AIControl#getPredictedPosition(float) ) } and 
     * {@link AIControl#getPredictedRotation() } to set it in any way you desire
     */
    DontApply
};
