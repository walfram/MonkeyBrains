package com.jme3.ai.agents;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 * The AIControl is the core of the Agents and Steering Behaviors.<br>
 * A more appropriate name would've been <code>AIObject</code> but I wanted to
 * clarify, that it's essentially just a simple jMonkeyEngine Control.<br>
 * <br>
 * The AIControl is necessary for everything which should be relevant to the AI.
 * An example for this is a mine which the AI should not walk into.<br>
 * Each Agent derives from this class.<br>
 * <br>
 * <hr>
 * <i>Note: This class replaces <code>com.jme3.ai.agents.util.GameEntity</code>
 * </i>
 * @author MeFisto94
 */
public class AIControl extends AbstractControl {

    float radius;
    boolean ignoreScenegraph = false;
    
    float maxMoveSpeed = 5f;
    float mass = 1f;
    //Vector3f acceleration = Vector3f.ZERO.clone();
    
    /**
     * The predicted Rotation is similar to the predicted Position.<br>
     * Use {@link #getPredictedRotation() } to access it.<br>
     * It is the Rotation the Unit should have after AI Calculations
     * 
     * @see #getPredictedPosition(float) 
     * @see #getPredictedRotation() 
     * @see #setPredictedRotation(com.jme3.math.Quaternion) 
     */ 
    Quaternion predictedRotation;
    
    
    /**
     * The Velocity is needed for the next updateAI cycle and is also required
     * to compute the next position
     */
    Vector3f velocity;
    
    /**
     * Instantiate a player, obstacle or whatever. We always assume a circular
     * shape, so you have to define a radius. The Center Point is considered the
     *  <code>spatial</code>'s <code>worldTranslation</code>.<br>
     * <br>
     * Example: The AI shall avoid an area surveiled by cops, then you implement
     * this area as a new AIControl with the desired radius.
     * 
     * @param radius The radius in game units
     */
    public AIControl(float radius) {
        this.radius = radius;
        predictedRotation = new Quaternion();
        velocity = Vector3f.ZERO.clone();
    }
    
    /**
     * Getter/setter for the fields
     */
    
    /**
     * Returns the radius of the circular area which is considered "this
     * object".<br> The Center Point is considered the <code>spatial</code>'s 
     * <code>worldTranslation</code>.<br>
     * 
     * @see #AIControl(float) 
     * @return The radius of this object.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the circular area which is considered "this object".
     * <br>
     * 
     * @see #getRadius() 
     * @see #AIControl(float) 
     * @param radius The radius of this object.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Returns whether this object is ignoring the SceneGraph's <code>controlUpd
     * ate</code> calls.<br>
     * See the appropriate setter-method for more informatikn
     * 
     * @see #setIgnoreScenegraph(boolean) 
     * @return Whether we are ignoring the SceneGraph.
     */
    public boolean isIgnoringScenegraph() {
        return ignoreScenegraph;
    }

    /**
     * Set whether this object should ignore the SceneGraph's
     * <code>controlUpdate</code> calls.<br>
     * This is useful when you want to run the AI in a seperate, decoupled
     * thread. In this case, you manually call {@link #updateAI(float) updateAI}
     * <br>
     * This also allows you to manually define which Object is updated how often
     * <br><br>
     * <i>Note: When not running in the MainThread you have to <code>enqueue
     * </code> some code. Look it up!</i>
     * 
     * @see #updateAI(float) 
     * @see Agent#setApplyType(com.jme3.ai.agents.ApplyType) 
     * @param ignoreScenegraph When set to true, we ignore the SceneGraph.
     */
    public void setIgnoreScenegraph(boolean ignoreScenegraph) {
        this.ignoreScenegraph = ignoreScenegraph;
    }

    /**
     * The predicted is the rotation that the unit should have after applying
     * the AI Calculations.<br> It might be different from the spatials actual
     * rotation (e.g. when the physics change something).<br>
     * <br>
     * Note: This is the World-Space Rotation<br>
     * <br>
     * @return The predicted rotation
     */
    public Quaternion getPredictedRotation() {
        return predictedRotation;
    }

    /**
     * <b>For Internal use only.</b> This sets the predicted rotation so it will
     * be called from the behaviors.<br>
     * <br>
     * Note: This is the World-Space Rotation<br>
     * <br>
     * @param rotation The rotation to set
     */
    public void setPredictedRotation(Quaternion rotation) {
        this.predictedRotation = rotation;
    }
   
    /**
     * Gets the Velocity of this Object. If you're in manual mode, you have to
     * move the object by this amount.<br>
     * <br>
     * If you want the actual movement speed of this object, see
     * {@link #getSpeed() }
     * 
     * @see #getSpeed() 
     * @return The Velocity of this Object.
     */
    public Vector3f getVelocity() {
        return velocity;
    }

    /**
     * Sets the Velocity of this Object. This is most likely only called from
     * internal code as there is no reason for external code to manipulate the
     * velocity apart from maybe collission feedback and setting an initial
     * value (and thus direction).<br>
     * <br>
     * Remember: The direction of the Vector defines the Direction of the object
     * and it's length defines the actual velocity (speed)
     * 
     * @param velocity The desired velocity
     */
    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    /**
     * Returns the maximum possible Movement Speed (capping it)
     * @return maximum speed
     */
    public float getMaxMoveSpeed() {
        return maxMoveSpeed;
    }

    /**
     * Sets the maximum possible movement speed (capping it)
     * @param maxMoveSpeed the maximum move speed in units per second
     */
    public void setMaxMoveSpeed(float maxMoveSpeed) {
        this.maxMoveSpeed = maxMoveSpeed;
    }

    /**
     * Returns the mass of this agent.<br>The mass is relevant for the
     * acceleration (how fast a behavior changes it's way)<br>
     * @return this unit's mass
     */
    public float getMass() {
        return mass;
    }

    /**
     * Sets the mass of this agent.<br>The mass is relevant for the
     * acceleration (how fast a behavior changes it's way)<br>
     * <br>
     * Don't think of it as kilograms but forces (g, gravity):<br>
     * A mass of 1/9.81 will lead to 1g pulling the character.<br>
     * A mass of 2/9.81 is 0.5g...
     * 
     * @see #getMass() 
     * @param mass The mass in kilogramms
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * Get the Acceleration of the Unit. This is the mass-corrected steering
     * force.<br>
     * <br>
     * This means, you can use mass to influence how fast new changes are
     * adopted.<br>
     * 
     * @see #setMass(float) 
     * @return The Acceleration of the unit
     */
    /*public Vector3f getAcceleration() {
        return acceleration;
    }*/

    /**
     * Sets the Acceleration of the unit.<br>
     * This is only for internal use (Each behavior sets the acceleration, so 
     * you can see it using {@link #getAcceleration() }<br>
     * <br>
     * @param acceleration The Acceleration which happens to the unit.
     */
    /*public void setAcceleration(Vector3f acceleration) {
        this.acceleration = acceleration;
    }*/
    
    /**
     * Some covenience/wrapper methods
     */
    
    /**
     * Returns the Position of this Unit <b>in the world</b>.<br>
     * 
     * @return The Position of this unit in the world
     */
    public Vector3f getWorldTranslation()
    {
        return spatial.getWorldTranslation();
    }
    
    /**
     * Position this unit in the correct world space.<br>This is mainly for 
     * internal use, since you might not blindly move the object but perform a 
     * colission check before or use Bullets <code>SetPhysicsLocation</code> 
     * instead.<br>
     * <br>
     * <i><b>Note:</b></i> You can use
     * {@link #worldToLocal(com.jme3.math.Vector3f) } if you need a local space
     * position for that.
     * @param translation The Position in world space where this unit's spatial
     * should be placed.
     * @see #setWorldRotation(com.jme3.math.Quaternion) 
     */
    public void setWorldTranslation(Vector3f translation)
    {
        spatial.setLocalTranslation(worldToLocal(translation));
    }
    
    /**
     * Returns the Rotation of this Unit <b>in the world</b>.<br>
     * 
     * @return The Rotation of this unit in the world
     */
    public Quaternion getWorldRotation()
    {
        return spatial.getWorldRotation();
    }
    
    /**
     * Rotates this unit in the correct world space.<br>
     * This is mainly for internal use<br>
     * <br>
     * <i><b>Note:</b></i> You can use
     * {@link #worldToLocal(com.jme3.math.Quaternion) } if you need a local
     * space rotation.
     * @param rotation The Rotation in world space
     * @see #setWorldTranslation(com.jme3.math.Vector3f) 
     */
    public void setWorldRotation(Quaternion rotation)
    {
        spatial.setLocalRotation(worldToLocal(rotation));
    }
    
    /**
     * Translates a Vector from World Space to Local Space. This is useful since
     * MonkeyBrains operates in world space but most objects are only placeable
     * in local space.
     * 
     * @param translation The World Space Translation
     * @return The Local Space Translation
     * @see #worldToLocal(com.jme3.math.Quaternion) 
     */
    public Vector3f worldToLocal(Vector3f translation)
    {
        if (spatial.getParent() == null)
        {
            return translation;
        }
        else
        {
            return translation.subtract(spatial.getParent().getWorldTranslation());
        }
    }
    
    /**
     * Translates a Quaternion from World Space to Local Space. This is useful 
     * since MonkeyBrains operates in world space but most objects are only 
     * modifiable in local space.
     * 
     * @param rotation The World Space Rotation
     * @return The Local Space Rotation
     * @see #worldToLocal(com.jme3.math.Quaternion) 
     */
    public Quaternion worldToLocal(Quaternion rotation)
    {
        if (spatial.getParent() == null)
        {
            return rotation;
        }
        else
        {
            /* First rotate the object against the parents rotation so 
             * we essentially have zero rotation again, then "apply" the
             * desired rotation.
             */
            return spatial.getParent().getWorldRotation().inverse().
                    multLocal(rotation);
        }
    }

    /**
     * This is the direction the unit is actually facing. It doesn't have to be
     * equal to the direction of the velocity (moving sideways).<br>
     * <br>
     * <i>Note: This uses the global rotation so if you rotate your model by 90°
     * in your Scenegraph somewhere, this will fail. Maybe I change it to local
     * again?</i><br>
     * 
     * @return The direction this unit is facing.
     */
    public Vector3f getForwardVector()
    {
        return spatial.getWorldRotation().mult(Vector3f.UNIT_Z).normalize();
    }
    
    /**
     * Calculates the "forwardness" in relation to another unit. That is
     * how "forward" is the direction to the quarry (1 means dead ahead, 0 is
     * directly to the side, -1 is straight back)
     *
     * @param aiControl the other unit
     * @see #forwardness(com.jme3.math.Vector3f) 
     * @return The forwardness in relation with another agent
     */
    public float forwardness(AIControl aiControl) {
        return forwardness(vectorTo(aiControl));
    }

    /**
     * Returns how much this unit is facing into the same direction as the
     * directionVector vector.
     * 
     * @param directionVector Offset vector.
     * @see #forwardness(com.jme3.ai.agents.AIControl) 
     * @return The forwardness in relation to the directionVector
     */
    public float forwardness(Vector3f directionVector) {
        /*
         * Some math here: cos phi = (u dot v) / ( |u| * |v|);
         * Since we're not interested in phi, we can save the costly inv cosine
         * we're just returning the cos phi. Note that this forwardness isn't
         * linear then, but this is how it was designed by previous authors.
         * 
         * You can linearize by calculating the inverse cosine (or directly use
         * Vector3f.angleBetween, it does the upper calculating along with said
         * inverse cosine) and then dividing by -PI and adding 1.
         * 
         * The question is if it's worth that, as you might not be able to tell
         * the difference, since the cosine is pretty linear appart from values
         * around 0, PI and 2PI (So forwardness is unprecise when telling
         * whether you walk with 99% into the same direction or only 98%.
         * This might as well be an expected behavior to be a bit "magnetic" 
         * around those values?
         * 
         */
        return          getForwardVector().dot(directionVector) /
                (getForwardVector().length() * directionVector.length());
    }
    
    /**
     * Calculates the Vector to another AIControl.<br>
     * This can be used for the distance or direction to <code>other</code><br>
     * <br>
     * @param other The unit to calculate the vector to.
     * @return The directional vector
     */
    public Vector3f vectorTo(AIControl other)
    {
        return AIControl.this.vectorTo(other.getWorldTranslation());
    }
    
    /**
     * Calculates the Vector to another AIControl.<br>
     * This can be used for the distance or direction to <code>other</code><br>
     * <br>
     * <i><b>Note:</b></i> This is only shorthand for <code>other.subtract(
     * getWorldTranslation());</code>
     * @param other The unit to calculate the vector to.
     * @return The directional vector
     */
    public Vector3f vectorTo(Vector3f other)
    {
        return other.subtract(getWorldTranslation());
    }
    
    /**
     * Calculates the distance to another AIControl.<br>
     * There is no hidden magic, it's just substracting the vectors and then
     * returning the length. It's only a convenience method.<br>
     * <br>
     * @param other The unit to calculate this distance to
     * @return the distance to <code>other</code>
     */
    public float distanceTo(AIControl other)
    {
        return vectorTo(other).length();
    }
    
    /**
     * Calculates the distance (normalized) to another AIControl.<br>
     * This does nothing other than <code>vectorTo(other).normalizeLocal()</code>.
     * <br><br>
     * @param other The AIControl to calculate the distance to.
     * @return The direction (normalized) to another AIControl
     */
    public Vector3f directionTo(AIControl other)
    {
        return vectorTo(other).normalizeLocal();
    }
    
    /* Helper Functions */
    
    /**
     * Returns the actual speed in world units, this object will move in this
     * frame.<br>
     * This value might also be 1, then the velocity is essentially the
     * direction and you manually have to multiply the vector.
     * <br>
     * Note: If you also want the direction, use {@link #getVelocity() }
     * 
     * @see #getVelocity() 
     * @return The Movement Speed of this Unit
     */
    public float getSpeed() {
        return velocity.length();
    }
    
    /**
     * Calculate the predicted position for this 'frame'<br>
     * Note: You have to call updateAI before (or let the SceneGraph do it)<br>
     * Note: This is in World Space.
     * 
     * @param tpf The Time since the last frame,
     * see {@link Control#update(float) }
     * @return The predicted position.
     */
    public Vector3f getPredictedPosition(float tpf) {
        if (velocity == null) {
            return spatial.getWorldTranslation();
        } else {
            return spatial.getWorldTranslation().add(velocity.mult(tpf));
        }
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if (!ignoreScenegraph) {
            updateAI(tpf);
        }
    }
    
    public void updateAI(float tpf) {
        /* Note: an AIControl doesn't have any logic to update, but this is here
         * to be overriden. 
         */
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // AI doesn't render anything.
    }

    @Override
    public String toString() {
        return "AIControl{" + "radius=" + radius + ", ignoreScenegraph=" + ignoreScenegraph + '}';
    }
    
    public static float angleBetween(Quaternion q1, Quaternion q2)
    {
        // this is some magic I found on the internetz.
        // see http://www.gamedev.net/topic/613685-find-angle-between-quaternion-a-and-b/
        return fastAcos(q2.mult(q1.inverse()).getW()) * 2f;
    }
    
    /**
     * Really fast acos approximation. <br>
     * Reducing much of the cpu load which was caused by angleBetween or 
     * {@link FastMath#acos(float) } which in turn only called
     * {@link Math#acos(double) } and hence operated on doubles. <br>
     * <br>
     * Note: NVidia claims an error of 6.7e-5 and for my test values I only 
     * saw deviations in the 8th decimal (so ~3e-8) compared to FastMath.<br>
     * <br>
     * Taken from <a href="http://http.developer.nvidia.com/Cg/acos.html">
     * http://http.developer.nvidia.com/Cg/acos.html</a><br>
     * <br>
     * 
     * @param x the value (which is clamped to [-1, 1])
     * @return the acos of x
     */
    public static float fastAcos(float x)
    {
        // Some clamping due to floating point errors (1.0000001)
        if (x > 1)
            x = 1;
        else if (x < -1)
            x = -1;
            
        // http://http.developer.nvidia.com/Cg/acos.html
        // Handbook of Mathematical Functions
        // M. Abramowitz and I.A. Stegun, Ed.

        // Absolute error <= 6.7e-5
        float negate = (x < 0) ? 1f : 0f;
        
        x = FastMath.abs(x);
        
        float ret = -0.0187293f;
        ret = ret * x;
        ret = ret + 0.0742610f;
        ret = ret * x;
        ret = ret - 0.2121144f;
        ret = ret * x;
        ret = ret + 1.5707288f;
        ret = ret * FastMath.sqrt(1.0f - x);
        ret = ret - 2 * negate * ret;
        return negate * 3.14159265358979f + ret;
    }
}
