
##Random Stuff:
`com.jme3.ai.agents.Team` has been removed. For my purposes it's nothing more than `List<Agent>`, since I don't need `points` or `name`.  
Also I removed `Agent`'s `team` field since for our understanding of "Agent Groups", one Agent can have multiple groups (Not sure how practical that will be when the behaviors interfer)  
  
`Agent` doesn't have a field `name` anymore and the matching constructors have been removed. The method `getName`, however, is still there. It returns the spatials name  
  
We now use the world rotation and translation everywhere. Is that good? I'm not sure yet. The World Rotation might be problematic when you fix your model by an intermediary 90° Node.  
On the other hand, when you have an agent standing on a car, you might want his rotation to be influenced by the car (and thus adding him as subnode of the car).  

Renamed the GameEntityException to AIControlException. This is part of a larger refactoring.  
Also renamed the GameEntitySeenEvent/Listener to AIControlSeenEvent/Listener.  
Removed Agent Total Mass (since we don't need the Inventory mass and it would be the users custom implementation anyway).  
  
calculateNewVelocity now uses `tpf`. Before this, the velocity field was wrong (and hence `getPredictedPosition`, ...). Also the velocity limitation was tpf unaware.  
`distanceToGameEntityRelative` is now called `distanceTo` and `offset` is now called `vectorTo`.  
`getMovementSpeed` is now called `getSpeed` since it returns the length of the velocity. But you still have `setMovementSpeed` and `setMaxMovementSpeed`
`fordwardVector` is now called `getForwardVector`, but might change to `getFacing`.  
`Behavior`s are no longer Controls and don't require an Agent in the Constructor. This is automatically propagated when the behavior is added to the Agent.  
  
Added `ApplyType` for more Control on how the AI Changes are applied. TODO: Physics related modes and something like `getPredictedRotation`  
Removed `getAcceleration` from the AIControl since multiple Behaviors would always overwrite it. That way you could only use it to find out if an behavior had steered ever.  
If that wasn't the case, the velocity would be zero anyway.  
Removed `Agent`'s `start`/`stop` since there already is `AbstractControl`s `setEnabled`.  
Bugfix: Made Position Prediction tpf aware (s = v * t; v = a * t; so essentially s = a * t^2 [Note: we're doing numerical integration/explicit euler here, hence we don't have 1/2 as correction factor, we sum it up].
For this to work  we simply use `AbstractSteeringBehavior`s getTimePerFrame. It has already been implemented by the original authors  

Made Rotation working by fixing the way the interpolation value is calculated. Now the rotationSpeed is in radians per second and also is correct (as in depends on the way to be done and
can reach the target velocity after a short time (where as the old one never seem'd to manage that). For this we introduced `getWorldRotation`, `setWorldRotation` and a Quaternion `worldToLocal`.
We now have a static method `AIControl.angleBetween(Quat1, Quat2)` and some `calculateNewRotation` in AbstractSteeringBehavior to set the `getPredictedRotation`.  

Bugfix: PathFollowBehavior doesn't need an ArrayList. It shouldn't care about the way the data is stored actually.  
PathFollowBehavior now features `setPathDoneRunable` to react to a finished path with a callback.  
Added a fast ACos Implementation since FastMath.acos was calling Math.acos which is butt slow.
Made `brakingFactor` available to AIControl. Only that way I could fix the braking bug (we were applying the Factor once per frame which zeroed our velocity) whilst still allowing different ApplyTypes.
This means `getPredictedPosition` is taking care of the brakingFactor.  
Normalized SeekBehaviors Steer Force.  

Arrive and Seek Behaviors now return Vector3f.ZERO when they are close enough to the target.  
BetterCharacterControl and RigidBodyControl are now supported as ApplyTypes. For some reason, the BetterCharacterControl doesn't look/work so nicely. I'll investigate that in a full blown project sooner or later.  

##Big Refactoring:
Essentially I renamed GameEntity to AIControl, since a) it's really a control and b) it should neither be mixed nor confused with an real ES. I then changed the implementation of some things.
I also removed unnecessary things (Hitpoints and such) from the Entity.
The code does compile now, but I still have to add some methods to have it work as expected and then overhaul the behaviors.  

##TODO:
Regarding the world rotation: getForwardVector currently returns the "facing", which is only a visual thing actually. When we would return the normalized velocity we'd have the direction the unit is walking.
That way we aren't prone to that "rotate by 90 degree to fix model" errors and it's up to the user where the agent is looking.
At least we should rename getForwardVector to "getFacing", but then the forwardVector is actually the facing, so how would one call the "WalkDirection"?  

Remove Agent from every Behavior, allowing them to be dynamically added to other agents but also don't require the agent field but get it using setAgent.