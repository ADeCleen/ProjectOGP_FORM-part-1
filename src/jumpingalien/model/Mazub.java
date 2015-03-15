package jumpingalien.model;


import jumpingalien.util.*;
import jumpingalien.common.sprites.*;

/**
 * A class of player characters, called Mazubs, involving a velocity, an acceleration, a location, a sprite
 * an idletime and multiple actions defined by booleans
 * 
 * @version	0.1
 * @author 	Toon
 *
 */
public class Mazub{
	
	//velocity and acceleration, expressed in pixels/s
	public double xSpeed;
	public final double xSpeedInit = 100;
	public final double xSpeedMax = 300;
	public final double xSpeedDucking = 100;
	public double ySpeed;
	public final double ySpeedInit = 800;
	public double xAccel;
	public final double xAccelStandard = 90;
	public double yAccel;
	public final double yAccelStandard = 1000;

	//booleans defining Mazub's actions
	public boolean isFalling;
	public boolean isDucking;
	public boolean isRunningLeft;
	public boolean isRunningRight;
	public boolean facingRight;
	public boolean facingLeft;
	public boolean idle = true;
	
	//positions, expressed in pixels
	public int xPos;
	public int yPos;
	public final int xPosMax = 1024 - 1;
	public final int yPosMax = 768  - 1;
	public double tempXPos;
	public double tempYPos;
	
	//variables for the sprites
	public Sprite currentSprite = JumpingAlienSprites.ALIEN_SPRITESET [0]; //first sprite as standard value
	public double idleTime;
	public int i; //index used when an action has multiple sprites
	public double tempTime;
	public double idleTemp;
	
//CALLABLES

	/**
	 * Initiates Mazub's movement to the right
	 * 
	 * @pre		Mazub is not moving right
	 * 			| !isRunningRight
	 * @effect	Mazub is moving right and not left
	 * 			| (isRunningRight && !(isRunningLeft))
	 * @post	Mazub is facing right 
	 * 			| (facingRight)
	 * @post	idle is false
	 * 			| !(idle)
	 */
	public void startMoveRight(){//nominaal
		assert (!isRunningRight);
		setXSpeed(xSpeedInit);
		setXAccel(xAccelStandard);
		isRunningRight = true;
		isRunningLeft = false;
		facingRight = true;
		facingLeft = false;
		idle = false;
	}
	
	/**
	 * Stops Mazub from moving to the right if he is moving to the right
	 * 
	 * @pre		Mazub must be moving right for this method to be called
	 * 			| isRunningRight
	 * @post	Mazub is not moving right 
	 * 			| !isRunningRight
	 */
	public void endMoveRight(){
		assert (isRunningRight);
		if (!(isRunningLeft)){ // if statement to protect Mazub's speed while running left, otherwise endMoveRight() would also stop movement to the left
			setXSpeed(0);
			setXAccel(0);
			isRunningRight = false;
		}
	}
	
	/**
	 * Initiates Mazub's movement to the left
	 * 
	 * @pre		Mazub must not be moving right for this method to be called
	 * 			| !isRunningRight
	 * @effect	Mazub is moving left and not right
	 * 			| (isRunningLeft && !(isRunningRight))
	 * @post	Mazub is facing left
	 * 			| (facingLeft)
	 * @post	idle is false
	 * 			| !(idle)
	 */
	public void startMoveLeft(){//nominaal
		assert (!isRunningLeft);
		setXSpeed(-xSpeedInit);
		setXAccel(-xAccelStandard);
		isRunningLeft = true;
		isRunningRight = false;
		facingLeft = true;
		facingRight = false;
		idle = false;
	}
	
	/**
	 * Stops Mazub from moving to the left if he is moving to the left
	 * 
	 * @pre		Mazub is moving left
	 * 			| isRunningLeft
	 * @post	Mazub is not moving left
	 * 			| !isRunningLeft
	 */
	public void endMoveLeft(){
		assert (isRunningLeft);
		if (!(isRunningRight)){
			setXSpeed(0);
			setXAccel(0);
			isRunningLeft = false;
		}
	}
	

	/**
	 * Starts Mazub's jumping action if he isn't already jumping or ducking
	 * 
	 * @post	Mazub is jumping
	 * 			| isFalling
	 * @throws 	ModelException if Mazub is already jumping or falling
	 * 			| isFalling
	 * @throws	ModelException is Mazub is ducking
	 * 			| isDucking
	 */
	public void startJump() throws ModelException{//defensief
		if (isFalling){
			throw new ModelException("Already jumping"); //will not stop the game because this does not form a problem
		}
		if (isDucking){
			throw new ModelException("Cannont jump while ducking"); //will not stop the game because this does not form a problem
		}
		else{
			setYSpeed(ySpeedInit);
			setYAccel(-yAccelStandard);
			isFalling = true;
		}
	}
	
	/**
	 * Stops Mazub's jump if he is jumping and lets him fall
	 * 
	 * @post	Mazub is falling and not jumping
	 * 			| new.getYSpeed() <= 0
	 * @throws 	ModelException if Mazub is not jumping or falling
	 * 			| !isFalling
	 */
	public void endJump() throws ModelException{//defensief
		if (!isFalling){
			throw new ModelException("Mazub is already not jumping"); //will not stop the game because this does not form a problem
		}
		if (getYSpeed() > 0){
			setYSpeed(0);
		}
	}
	
	/**
	 * Initiates Mazub's ducking action
	 * 
	 * @post	Mazub is ducking
	 * 			| isDucking = true;
	 * @throws 	ModelException when Mazub is already ducking
	 * 			| isDucking = true
	 * @throws 	ModelException when Mazub is jumping
	 * 			| isFalling = true
	 */
	public void startDuck() throws ModelException{//defensief
		if (isDucking){
			throw new ModelException("Already Ducking"); //will not stop the game because this does not form a problem
		}
		if (isFalling){
			throw new ModelException("Cannot duck while jumping"); //will not stop the game because this does not form a problem
		}
		else{
			isDucking = true;
		}
	}
	
	/**
	 * Stops Mazubs ducking movement if he's ducking
	 * 
	 * @post	Mazub is not ducking
	 * 			| isDucking = false
	 * @throws 	ModelException if Mazub is not ducking
	 * 			| (!(isDucking))
	 */
	public void endDuck() throws ModelException{//defensief
		if (!isDucking){
			throw new ModelException("Mazub is not ducking"); //will not stop the game because this does not form a problem
		}
		else {
			isDucking = false;
		}
	}

//HELPER METHODS
	
	/**
	 * Stops any vertical movement Mazub has if he has any
	 * 
	 * @effect	Mazub has not vertical movement
	 * 			| ((new.getYSpeed() == 0) && (new.getYAccel() == 0))
	 */
	private void stopYMovement(){
		if (isFalling){
			setYSpeed(0);
			setYAccel(0);
			isFalling = false;
		}
	}
	
	/**
	 * Stops any horizontal movement Mazub has if he has any
	 * 
	 * @effect	Mazub as no horizontal movement
	 * 			| ((new.getXSpeed() == 0) && (new.getXAccel() == 0))
	 */
	private void stopXMovement(){
		if ((isRunningLeft) || (isRunningRight)){
			setXSpeed(0);
			setXAccel(0);
			isRunningRight = false;
			isRunningLeft = false;
		}
	}
	
	/**
	 * @param  	deltaTime
	 * 			the change of time used to calculate the speed
	 * @pre		deltaTime is a valid double
	 * 			| (isValidDouble(deltaTime))
	 * @return	the new value of yTempSpeed
	 * 			| (getYSpeed() + getYAccel()*deltaTime)
	 */
	private double ySpeedFormula(double deltaTime){
		assert isValidDouble(deltaTime);
		return (getYSpeed() + getYAccel()*deltaTime);
	}
	
	/**
	 * @param 	deltaTime
	 * 			the change of time used to calculate the speed
	 * @pre 	deltaTime is a valid double
	 * 			| (isValidDouble(deltaTime))
	 * @return	the new value of xTempSpeed
	 * 			| (getXSpeed() + getXAccel()*deltaTime)
	 */
	private double xSpeedFormula(double deltaTime){
		assert isValidDouble(deltaTime);
		return (getXSpeed() + getXAccel()*deltaTime);
	}
	
	/**
	 * Calculatse the change of horizontal position
	 * 
	 * @param 	deltaTime
	 * 			the change of time used to calculate the position
	 * @pre		deltaTime is a valid double
	 * 			| isValidDouble(deltaTime)
	 * @return	the temporary change of position 	
	 * 			| (getXSpeed()*deltaTime) + ((getXAccel()/2) * deltaTime * deltaTime)
	 */
	private double xPosFormula(double deltaTime){
		assert isValidDouble(deltaTime);
		return ((getXSpeed()*deltaTime) + ((getXAccel()/2) * deltaTime * deltaTime));
	}
	
	/**
	 * Calculates change of vertical position
	 * 
	 * @param 	deltaTime
	 * 			the change of time used to calculate the position
	 * @pre 	deltaTime is a valid double
	 * 			| isValidDouble(deltaTime)
	 * @return	the temporary change of position
	 * 			| (getYSpeed()*deltaTime) + ((getYAccel()/2) * deltaTime * deltaTime)
	 */
	private double yPosFormula(double deltaTime){
		assert isValidDouble(deltaTime);
		return ((getYSpeed()*deltaTime) + ((getYAccel()/2) * deltaTime * deltaTime));
	}
	
	/**
	 * Sets the horizontal movement to the maximum allowed value
	 * 
	 * @param 	sign
	 * 			determins wether Mazub is going left or right
	 * @post	if the sign is + Mazub has his maximum horizontal velocity to the right
	 * 			| if (sign == '+')
	 * 			|	new.getXSpeed() == xSpeedMax
	 * @post 	if the sign is - Mazub will get his maximum horizontal velocity to the left
	 * 			| if (sign == '-')				
	 * 			|	new.getXSpeed() == -xSpeedMax
	 * @post	Mazub's horizontal acceleration is 0
	 * 			| new.getXAccel() == 0
	 */
	private void xSpeedMaxReached(char sign){
		if (sign == '+'){
			setXSpeed(xSpeedMax);
			setXAccel(0);
		}	
		if (sign == '-'){
			setXSpeed(-xSpeedMax);
			setXAccel(0);
		}
	}

	/**
	 * Checks wether or not the given value is a valid double
	 * 
	 * @param 	value
	 * 			the value that this method will test
	 * @return	a boolean statement, true when it passes, false when it fails
	 * 			| (value == (double)value)
	 */
	private boolean isValidDouble(double value){
		return (value == (double)value);
	}
	
	/**
	 * Checks wether or not the given value is a valid integer
	 * 
	 * @param 	value
	 * 			the value that this method will test
	 * @return	a boolean statement, true when it passes, false when it fails
	 * 			| (value == (int) value)
	 */
	private boolean isValidInteger(int value){
		return (value == (int) value);
	}
	

//SETTERS	
	
	/**
	 * Sets the horizontal speed to the calculated value
	 * 
	 * @param 	value 
	 * 			the new horizontal speed for Mazub
	 * @pre		the absolute value of xSpeed doesn't exceed the maximum allowed value
	 * 			| (value >= -xSpeedMax) && (value <= xSpeedMax)
	 * @pre		the new horizontal speed is a valid double
	 * 			| isValidDouble(value)
	 * @post	the new xSpeed is the given value
	 * 			| new.getXSpeed() == value
	 */
	private void setXSpeed(double value){ 
		assert (isValidDouble(value));
		assert ((value >= -xSpeedMax) && (value <= xSpeedMax));
		xSpeed = value;
	}
	
	/**
	 * Sets the vertical speed the calculated value
	 * 
	 * @param 	value 
	 * 			the new vertical speed for Mazub
	 * @pre		the value is a valid double
	 * 			| (isValidDouble(value))
	 * @post	the new ySpeed is the given value
	 * 			| new.getYSpeed() == value
	 */
	private void setYSpeed(double value){
		assert isValidDouble(value);
		ySpeed = value;
	}
	
	/**
	 * Sets the horizontal acceleration to the given value
	 * 
	 * @param 	value 
	 * 			the new horizontal acceleration for Mazub
	 * @pre		value is a valid double
	 * 			| isValidDouble(value)
	 * @post	the new xAccel is the given value
	 * 			| new.getXAccel() == value
	 */
	private void setXAccel(double value){
		assert isValidDouble(value);
		xAccel = value;
	}
	
	/**
	 * Sets the vertical acceleration to the given value
	 * 
	 * @param 	value 
	 * 			the new vertical acceleration for Mazub
	 * @pre		value is a valid double
	 * 			| isValidDouble(value)
	 * @post	the new yAccel is the given value
	 * 			| new.getYAccel() == value
	 */
	private void setYAccel(double value){
		assert isValidDouble(value);
		yAccel = value;
	}
	
	/**
	 * Sets the current horizontal position to the given value
	 * 
	 * @param 	value 
	 * 			the new horizontal position for Mazub
	 * @post	the new xPos is equal to the value given to the method
	 * 			| new.getXPos() == value
	 * @throws	IllegalArgumentException when the given value is not an integer
	 * 			| !(value == (int)value)
	 */ 
	private void setXPos(int value) throws IllegalArgumentException{
		if ((!isValidInteger(value)))
			throw new IllegalArgumentException(); //stops the game because this is never supposed to happen
		xPos = value;
	}
	
	/**
	 * Sets the current vertical position to the given value
	 * 
	 * @param 	value 
	 * 			the new vertical position that Mazub has
	 * @post	the new yPos is equal to the value given to the method
	 * 			| new.getYPos() == value
	 * @throws	IllegalArgumentException when the given value is not an integer
	 * 			| !(isValidInteger(value))
	 */
	private void setYPos(int value) throws IllegalArgumentException{
		if (!(isValidInteger(value)))
			throw new IllegalArgumentException();
		yPos = value;
	}
	

	/**
	 * Sets the current sprite to the given sprite
	 * 
	 * @param 	sprite
	 * 			the sprite that describes Mazub's current action
	 * @post	the new sprite is equal to the sprite given to this method
	 * 			| new.getCurrentSprite() == sprite
	 * @throws	IllegalArgumentException when sprite is not of type Sprite
	 * 			| !(sprite instanceof Sprite)
	 */
	private void setCurrentSprite(Sprite sprite) throws IllegalArgumentException{
		if (!(sprite instanceof Sprite))
			throw new IllegalArgumentException("cannot display given argument");
		currentSprite = sprite;
	}
	
	/**
	 * Sets the idle time to the given value
	 * 
	 * @param 	value
	 * 			the new idleTime for Mazub
	 * @pre		the given value is a valid double
	 * 			| assert (isValidDouble(value))
	 * @post	the new value of idleTime is equal to the given value
	 * 			| idleTime == value
	 */
	private void setIdleTime(double value){
		assert (isValidDouble(value));
		idleTime = value;
	}
	
	//GETTERS	
	
		/**
		 * Gives the current horizontal speed
		 * 
		 * @throws	IllegalArgumentException when xSpeed is not a valid double
		 * 			| !(isValidDouble(xSpeed))
		 * @return	the horizontal speed
		 * 			| return xSpeed
		 */
		public double getXSpeed() throws IllegalArgumentException{
			if(!(isValidDouble(xSpeed)))
				throw new IllegalArgumentException();
			return xSpeed;
		}
		
		/**
		 * Gives the current vertical speed
		 * 
		 * @return	the vertical speed
		 * 			| return ySpeed
		 * @throws 	IllegalArgumentException when ySpeed is not a valid double
		 * 			| !(isValidDouble(ySpeed))
		 */
		public double getYSpeed() throws IllegalArgumentException{
			if (!(isValidDouble(ySpeed)))
				throw new IllegalArgumentException();
			return ySpeed;
		}
		
		/**
		 * Gives the current horizontal acceleration
		 * 	
		 * @return	the horizontal acceleration			
		 *			| return xAccel
		 * @throws 	IllegalArgumentException when xAccel is not a valid double	
		 *			| !(isValidDouble(xAccel))
		 */
		public double getXAccel() throws IllegalArgumentException{
			if (!(isValidDouble(xAccel)))
				throw new IllegalArgumentException();
			return xAccel;
		}
		
		/**
		 * Gives the current vertical acceleration
		 * 
		 * @return	the vertical acceleration
		 * 			| return yAccel
		 * @throws 	IllegalArgumentException when yAccel is not a valid double
		 * 			| !(isValidDouble(yAccel))
		 */
		public double getYAccel() throws IllegalArgumentException{
			if (!(isValidDouble(yAccel)))
				throw new IllegalArgumentException();
			return yAccel;
		}
		
		/**
		 * Gives the current horizontal position
		 * 
		 * @return	the horizontal position	
		 * 			| return xPos
		 * @throws	IllegalArgumentException when xPos is not a valid integer
		 * 			| !(isValidInteger(xPos))
		 */
		public int getXPos() throws IllegalArgumentException{
			if(!(isValidInteger(xPos)))
				throw new IllegalArgumentException();
			return xPos;
		}
		
		/**
		 * Gives the current vertical position
		 * 
		 * @return	the vertical position
		 * 			| return yPos
		 * @throws	IllegalArgumentException when yPos is not a valid integer
		 * 			| !(isValidInteger(yPos))
		 */
		public int getYPos() throws IllegalArgumentException{
			if (!(isValidInteger(yPos)))
				throw new IllegalArgumentException();
			return yPos;
		}
		
		
		/**
		 * Gives the size of Mazub (the size of his sprite)
		 * 
		 * @return 	returns the size of Mazub in pixels
		 * 			| return (int[] size = {getCurrentSprite().getHeight(), getCurrentSprite().getWidth()})
		 * 
		 * @post	The size of Mazub is the size of his current sprite
		 * 			| size == {getCurrentSprite().getHeight(), getCurrentSprite().getWidth()}
		 * 
		 * @throws	ModelException when there is no sprite to get the size from
		 * 			| getCurrentSprite() == null
		 * 
		 * @throws	IllegalArgumentException when the used getCurrentSprite does not return a Sprite
		 * 			| !(getCurrentSprite() instanceof Sprite)
		 */
		public int[] getSize() throws ModelException, IllegalArgumentException{
			if (getCurrentSprite() == null)
				throw new ModelException("no sprite to select size from");
			if (!(getCurrentSprite() instanceof Sprite))
				throw new IllegalArgumentException("cannot get size from something that is not a sprite");
			int[] size = {getCurrentSprite().getWidth(), getCurrentSprite().getHeight()};
			return size;
		}
		
		/**
		 * Gives the current velocity of Mazub, getXSpeed and getYSpeed are already programmed so that they can only a valid value
		 * 
		 * @Post 	velocity consists of the current xSpeed and the current ySpeed
		 * 			| double [] velocity == {getXSpeed()/100, getYSpeed()/100}
		 * 
		 * @return	the horizontal and vertical velocity in [m/s] as an array of doubles
		 * 			| return velocity
		 */
		public double[] getVelocity(){
			double[] velocity = {getXSpeed()/100, getYSpeed()/100}; //1m = 100 pixels
			return velocity;
		}
		
		/**
		 * Gives the current location of mazub as an array of integers
		 * 
		 * @return	returns the location of the lowest, leftmost pixel of Mazub as and array of integers containing xPos and yPos
		 * 			| return (int[] location = {(int)getXPos(), (int)getYPos()})
		 * @throws 	ModelException when Mazub tries to go out of bounds
		 * 			| (getXPos() < 0) || (getXPos() > xPosMax) || (getYPos() < 0) || (getYPos() > yPosMax)
		 * @throws	IllegalArgumentException when and of the position values aren't intergers
		 * 			| (!(isValidInteger(getXPos()) || !(isValidInteger(getYPos()))))
		 * @post	location is an array of integeres that contains the current position of Mazub
		 * 			| int[] location == {getXPos(), getYPos()}
		 */
		public int[] getLocation() throws ModelException, IllegalArgumentException{//should not be possible
			if ((getXPos() < 0) || (getXPos() > xPosMax) || (getYPos() < 0) || (getYPos() > yPosMax))
				throw new ModelException("out of bounds"); //should not be possible
			if (!(isValidInteger(getXPos()) || !(isValidInteger(getYPos()))))
				throw new IllegalArgumentException("positions can only be integers");
			int[] location = {getXPos(), getYPos()};
			return location;
		}
		
		/**
		 * Gives the acceleration of Mazub as an array of doubles
		 * 
		 * @return	the horizontal and vertical acceleration of Mazub as an array of doubles containing xAccel and yAccel
		 * 			| return (acceleration)
		 * @pre		the horizontal acceleration must be a valid double for this method to work
		 * 			| isValidDouble(getXAccel())
		 * @pre		the vertical acceleration must be a valid double for this method to work
		 * 			| isValidDouble(getYAccel())
		 */
		public double[] getAcceleration(){
			assert isValidDouble(getXAccel());
			assert isValidDouble(getYAccel());
			double[] acceleration = {getXAccel(), getYAccel()};
			return acceleration;
		}
		
		/**
		 * Gives the sprite of Mazub's current action
		 * 
		 * @return	the current sprite for Mazub's movement
		 * 			| return currenSprite
		 * @pre 	currentSprite is of type Sprite
		 * 			| assert (currentSprite instanceof Sprite)
		 */
		public Sprite getCurrentSprite(){
			assert (currentSprite instanceof Sprite);
			return currentSprite;
		}
		
		/**
		 * Gives the time since the last horizontal movement
		 * 
		 * @pre		the current idleTime is a valid double
		 * 			| isValidDouble(idleTime)
		 * @return	the time since Mazub's last horizontal movement
		 * 			| return idleTime
		 */
		public double getIdleTime(){
			return idleTime;
		}
		

		
	
//UPDATERS	
	
	
	/**
	 * Calculates the new horizontal speed and acceleration and calls the setters with the calculated values as argument
	 * 
	 * @param 	deltaTime
	 * 			the time that has passed since the last update
	 * @post	if deltaTime is not a valid value it is set to 0
	 * 			| if (deltaTime != (double) deltaTime)
	 * 			|	deltaTime == 0;
	 * @effect	if Mazub is Running and ducking the speed is the predefined value and the acceleration is 0
	 * 			| if ((isDucking) && (isRunningRight))
	 *			|	new.getXSpeed() == xSpeedDucking
	 *			| 	new.getXAccel() == 0
	 *			| if ((isDucking) && (isRunningLeft))
	 *			|	new.getXSpeed() == -xSpeedDucking
	 *			|	new.getXAccel() == 0
	 * @post	if Mazub is not ducking this method calls another method to calculate the new speed
	 *			| xTempSpeed == xSpeedFormula(deltaTime)
	 * @effect	if xTempSpeed exceeds the predefined maximum xSpeedMaxReached is called and given a sign 
	 * 			to indicate if Mazub is going right or left, this means that the new xSpeed is the maximum allowed horizontal speed
	 * 			| new.getXSpeed() == xSpeedMax
	 * 			| new.getXSpeed() == -xSpeedMax
	 * @post	if the calculated speed doesn't exceed the maximum value, this method calls setXSpeed
	 * 			| this means that the new xSpeed is the calculated value
	 * 			| new.getXSpeed() == xTempSpeed
	 */
	private void xMovementUpdate(double deltaTime){//totaal
		if (!(isValidDouble(deltaTime)))
			deltaTime = 0;
		if ((isDucking) && (isRunningRight))
			setXSpeed(xSpeedDucking);
		if ((isDucking) && (isRunningLeft))
			setXSpeed(-xSpeedDucking);
		else{
			double xTempSpeed = xSpeedFormula(deltaTime);
			if (xTempSpeed >= xSpeedMax)
				xSpeedMaxReached('+');
			if (xTempSpeed <= -xSpeedMax)
				xSpeedMaxReached('-');
			else
				setXSpeed(xTempSpeed);
		}
	}
	
	/**
	 * Calculates the vertical speed and acceleration and calls the setters with the calculates values as argument
	 * 
	 * @param  	deltaTime
	 * 			the time that has passed since the last update	
	 * @post	if deltaTime is not a valid value it is set to zero so it doesn't wrongly update the speed
	 * 			| if (!(isValidDouble(deltaTime)))
	 * 			|	deltaTime == 0
	 * @post	if the vertical position goes under 0 the method stopYMovement() is called, this ensures that the vertical movement stops completely
	 * 			| if (getYPos() < 0)
	 * 			|	new.getYSpeed() == 0
	 * 			|	new.getYAccel() == 0
	 * @post	otherwise a new ySpeed is calculated and set as new speed
	 * 			| new.getYSpeed() == yTempSpeed
	 */
	private void yMovementUpdate(double deltaTime){ //totaal
		if (!(isValidDouble(deltaTime)))
			deltaTime = 0;
		if (getYPos() < 0){
			stopYMovement();
		}
		else{
			double yTempSpeed = ySpeedFormula(deltaTime);
			setYSpeed(yTempSpeed);
		}
	}
	
	/**
	 * Calculates the new horizontal position and calls the setter with the calculated value as argument
	 * 
	 * @param 	deltaTime
	 * 			the time that has passed since the last update
	 * @throws	IllegalArgumentException if deltaTime is not a valid double		
	 * 			| !(isValidDouble(deltaTime))
	 * @post	if the calculated new horizontal position doesn't go out of bounds (under 0 or above the maximum value)
	 * 			the new xPos is equal to the calulcated value rounded down to an integer plus the old xPos
	 * 			| if (!((getXPos() + tempXPos) < 0) && !((getXPos() + tempXPos) > xPosMax))
	 * 			| 	new.getXPos() == xPos + (int)tempXPos
	 * @effect	if it does go out of bounds the horizontal movement is stopped and the yPos is not updated
	 * 			| new.getXPos() == xPos
	 * 			| new.getXSpeed() == 0
	 * 			| new.getYAccel() == 0
	 * @invar	the xPos is always an integer
	 * 			| isValidInteger(getXPos())
	 */
	private void xPosUpdate(double deltaTime) throws IllegalArgumentException{
		if (!(isValidDouble(deltaTime)))
			throw new IllegalArgumentException();
		double deltaX  = (xPosFormula(deltaTime));
		tempXPos += deltaX;
		if (!((getXPos() + tempXPos) < 0) && !((getXPos() + tempXPos) > xPosMax)){
			setXPos(getXPos() + (int)tempXPos);
			tempXPos = 0;
		}
		else{
			stopXMovement();
			tempXPos = 0;
		}
	}
	
	/**
	 * Calculates the new vertical position and calls the setter with the calculated value as argument
	 * 
	 * @param 	deltaTime
	 * 			the time that has passed since the last update
	 * @throws 	IllegalArgumentException if deltaTime is not a valid double
	 * 			| !(isValidDouble(deltaTime))
	 * @post	if the the new vertical position doens't go out of bounds (under 0 or above the maximum value)
	 * 			the new yPos is equal to the calculated value rounded to an integer plus the old yPos
	 * 			| if (!((getYPos() + tempYPos) <= 0) && !((getYPos() + tempYPos) > yPosMax))
	 * 			|	new.getYPos() == yPos + (int) tempYPos
	 * @effect	if the position goes above the maximum value the new yPos is set to the maximum value
	 * 			and endJump() is called so Mazub is falling
	 * 			| if ((getYPos() + tempYPos) > yPosMax)
	 * 			|	new.getYSpeed <= 0
	 * 			|	new.getYPos() == yPosMax
	 * @effect	if the position goes below 0, it is set to 0 and vertical movement is stopped completely
	 * 			| if ((getYPos() + tempYPos <= 0))
	 * 			| 	new.getYSpeed() == 0
	 * 			| 	new.getYAccel() == 0
	 * 			|	new.getYPos() == 0
	 * @invar	the yPos is always an integer
	 * 			| isValidInter(getYPos())
	 */
	private void yPosUpdate(double deltaTime) throws IllegalArgumentException{
		if (!(isValidDouble(deltaTime)))
			throw new IllegalArgumentException();
		double deltaY = (yPosFormula(deltaTime));
		tempYPos += deltaY;
		if (!((getYPos() + tempYPos) <= 0) && !((getYPos() + tempYPos) > yPosMax)){
			setYPos(getYPos() + (int)tempYPos);
			tempYPos = 0;
		}
		else{
			if ((getYPos() + tempYPos) > yPosMax){
				endJump();
				setYPos(yPosMax);
			}
			if ((getYPos() + tempYPos <= 0)){
				stopYMovement();
				setYPos(0);
			}
			tempYPos = 0;
		}
	}
	
	/**
	 * Calculates the new idle time and calls the setter with the calculated value as argument
	 * 
	 * @param 	deltaTime
	 * 			the time since the last update
	 * @post	if deltaTime is not a valid value it is set to 0
	 * 			| if (deltaTime != (double) deltaTime)
	 *			|	deltaTime == 0
	 * @effect	if the idleTime is equal to or greater than 1 second, idle is and Mazub is not facing any direction
	 * 			| if (getIdleTime() >= 1)
	 * 			|	idle == true
	 * 			|	facingRight == false
	 * 			|	facingLeft == false
	 * @effect	if Mazub is currently performing an action the idleTime is reset and idle is false
	 * 			| new.getIdleTime() == 0
	 * 			| idle == false
	 * 
	 */
	private void idleTimeUpdate(double deltaTime){
		if (!isValidDouble(deltaTime))
				deltaTime = 0;
		idleTemp = (getIdleTime() + deltaTime);
		if ((!isRunningRight) && (!isRunningLeft)){
			setIdleTime(idleTemp);
			if (getIdleTime() >= 1){
				idle = true;
				facingRight = false;
				facingLeft = false;
			}
		}
		else {
			setIdleTime(0);
			idle = false;
		}
	}
	
	/**
	 * Calculates the time since the last sprite update
	 * 
	 * @param 	deltaTime
	 * 			the that has passed since the last update of this method
	 * @post	if tempTime exceeds 0.075 seconds, spriteSelect is called
	 * 			| if (tempTime >= 0.075)
	 * 			|	spriteSelect()
	 */
	private void spriteUpdate(double deltaTime){
		tempTime += deltaTime;
		if (tempTime >= 0.075){ 
			spriteSelect();
			tempTime = 0;
		}	
	}
	
//advanceTime
	/**
	 * Calls all updater methods with the time since the last update as argument
	 * 
	 * @param 	dt
	 * 			the time since the last update
	 * @throws	IllegalArgumentException when dt is not a double
	 * 			| dt != (double)dt
	 * @throws 	IllegalArgumentException when dt is under 0 seconds, refresh time can never be negative
	 * 			| dt < 0
	 * @throws	IllegalArgumentException when dt is above 0.2 seonds, refresh time is never be above 0.2 seconds 
	 * 			| dt > 0.2
	 * @effect	everything (yPos, xPos, xMovement, yMovement, sprite (has a built in timerestriction), idleTime) is update and 
	 * 			advanced by the given time
	 * 			| yPosUpdate(dt); 	
	 *			| xPosUpdate(dt);		
  	 *			| yMovementUpdate(dt); 
	 *			| xMovementUpdate(dt); 	
	 *			| spriteUpdate(dt); 		
	 *			| idleTimeUpdate(dt);	
	 */
	public void advanceTime(double dt) throws IllegalArgumentException{ //defensief
		if (!(isValidDouble(dt)))
			throw new IllegalArgumentException("The given value is not valid");
		if (dt < 0)
			throw new IllegalArgumentException("Refresh time cannot be negative");
		if (dt > 0.2)
			throw new IllegalArgumentException("Refresh time may not be above 0.2 second");
		yPosUpdate(dt); 		//updates the vertical position
		xPosUpdate(dt);			//updates the horizontal position
		yMovementUpdate(dt); 	//updates the horizontal speed and acceleration
		xMovementUpdate(dt); 	//updates the vertical speed and acceleration
		spriteUpdate(dt); 		//updates the current sprite
		idleTimeUpdate(dt);	 	//updates the idle time
	}


//spriteIndexer
	/**
	 * Checks what action Mazub is currently performing
	 * 
	 * @post	the currentSprite is updated to the right one using booleans to recognise the current action
	 * 			| if (booleans)
	 * 			| 	new.getCurrentSprite() == (JumpingAlienSprites.ALIEN_SPRITESET[right index])
	 * @post	if Mazub is performing an action that has multiple sprites (e.g. running right) the right sprite is selected using variable i
	 * 			when this variable exceeds the total amount of sprites for the movement it is reset and the first sprite is shown again
	 * 			| if (i > 10)
	 *			|	i -= 10;
	 *			| new.getCurrentSprite() == (JumpingAlienSprites.ALIEN_SPRITESET[startindex + i])
	 */
	private void spriteSelect(){
		if ((idle) || (!isDucking) && (getXSpeed() == 0) && (getYSpeed() == 0)){
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[0]);
		}
		if ((isDucking) && (!facingLeft) && (!facingRight) && (!idle)){
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[1]);
		}
		if ((!isDucking) && (facingRight) && (getXSpeed() == 0) && (!idle)){
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[2]);
		}
		if ((!isDucking) && (facingLeft) && (!idle)){
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[3]);
		}
		if ((isFalling) && (facingRight) && (yPos > 0) && (!idle)){
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[4]);
		}
		if ((isFalling) && (facingLeft) && (yPos > 0) && (!idle)){
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[5]);
		}
		if ((isDucking) && (facingRight) && (!idle)){
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[6]);
		}
		if ((isDucking) && (facingLeft) && (!idle)){
			setCurrentSprite (JumpingAlienSprites.ALIEN_SPRITESET[7]);
		}
		if ((isRunningRight) && (!isFalling) && (!isDucking) && (!idle)){
			if (i > 10)
				i -= 10;
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[8 + i]);
			i++;
		}	
		if ((isRunningLeft) && (!isFalling) && (!isDucking) && (!idle)){
			if (i > 10)
				i -= 10;
			setCurrentSprite(JumpingAlienSprites.ALIEN_SPRITESET[19 + i]);
			i++;		
		}		
	}
}
