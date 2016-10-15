
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
`distanceToGameEntityRelative` is now called `distanceTo` and `offset` is now called `vectorTo`
`getMovementSpeed` is now called `getSpeed` since it returns the length of the velocity. But you still have `setMovementSpeed` and `setMaxMovementSpeed`
`fordwardVector` is now called `getForwardVector`, but might change to `getFacing`.
`Behavior`s are no longer Controls and don't require an Agent in the Constructor. This is automatically propagated when the behavior is added to the Agent.

Added `ApplyType` for more Control on how the AI Changes are applied. TODO: Physics related modes and something like `getPredictedRotation`

##Big Refactoring:
Essentially I renamed GameEntity to AIControl, since a) it's really a control and b) it should neither be mixed nor confused with an real ES. I then changed the implementation of some things.
I also removed unnecessary things (Hitpoints and such) from the Entity.
The code does compile now, but I still have to add some methods to have it work as expected and then overhaul the behaviors.  

##TODO:
Change Rotation Speed so it depends on the actual way that has to be done. Currently, the character rotates like 50% no matter how much that is.
On the other hand, if you do it realisticly, it might take longer and currently I have no idea on how to measure the angle between two quaternions only on around the Y axis
(Or say it's a bit more complex).  
I might have to test how irrealisticly this would look

Regarding the world rotation: getForwardVector currently returns the "facing", which is only a visual thing actually. When we would return the normalized velocity we'd have the direction the unit is walking.
That way we aren't prone to that "rotate by 90 degree to fix model" errors and it's up to the user where the agent is looking.
At least we should rename getForwardVector to "getFacing", but then the forwardVector is actually the facing, so how would one call the "WalkDirection"?  

Remove Agent from every Behavior, allowing them to be dynamically added to other agents but also don't require the agent field but get it using setAgent.