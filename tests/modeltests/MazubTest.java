package modeltests;

import static org.junit.Assert.*;

import static tests.jumpingalien.tests.util.TestUtils.doubleArray;
import static tests.jumpingalien.tests.util.TestUtils.intArray;
import jumpingalien.model.Mazub;
import jumpingalien.util.Sprite;
import jumpingalien.util.Util;

import org.junit.Test;

public class MazubTest {
	
	@Test
	public void moveRightCorrectly(){
		Mazub mazub = new Mazub();
		mazub.startMoveRight();
		mazub.advanceTime(0.1);
		
		// x_new [m] = 0 + 1 [m/s] * 0.1 [s] + 1/2 0.9 [m/s^2] * (0.1 [s])^2 =
		// 0.1045 [m] = 10.45 [cm], which falls into pixel (10, 0)
		assertArrayEquals(intArray(10, 0), mazub.getLocation());
		assertTrue(mazub.isRunningRight);
	}
	
	@Test
	public void endMoveRightCorrectly(){
		Mazub mazub = new Mazub();
		mazub.startMoveRight();
		mazub.advanceTime(0.1);
		assertArrayEquals(intArray(10, 0), mazub.getLocation());
		assertTrue(mazub.isRunningRight);
		
		// Mazub's position should not change after endMoveRight() is invoked, even though we advance time
		mazub.endMoveRight();
		mazub.advanceTime(0.1);
		assertArrayEquals(intArray(10, 0), mazub.getLocation());
		assertFalse(mazub.isRunningRight);
		assertTrue(mazub.getXSpeed() == 0);
		assertTrue(mazub.getXAccel() == 0);
	}
	
	@Test
	public void maxSpeedAtRightTime(){
		Mazub mazub = new Mazub();
		mazub.startMoveRight();
		// maximum speed reached after 20/9 seconds
		for (int i = 0; i < 100; i++) {
			mazub.advanceTime(0.2 / 9);
		}
		assertArrayEquals(doubleArray(3, 0), mazub.getVelocity(), Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void doesntExceedMaxSpeed(){
		Mazub mazub = new Mazub();
		mazub.startMoveRight();
		// maximum speed reached after 20/9 seconds, so if we advance more then the speed should stay the same
		for (int i = 0; i < 100; i++) {
			mazub.advanceTime(0.2 / 9);
		}
		assertArrayEquals(doubleArray(3, 0), mazub.getVelocity(), Util.DEFAULT_EPSILON);
		double[] old = mazub.getVelocity();
		
		mazub.advanceTime(0.1);
		assertArrayEquals(old, mazub.getVelocity(), Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void moveLeftCorrectly(){
		Mazub mazub = new Mazub();
		mazub.startMoveRight();
		mazub.advanceTime(0.2);
		
		// x_new [m] = 0 + 1 [m/s] * 0.2 [s] + 1/2 0.9 [m/s^2] * (0.2 [s])^2 =
		// 0.218[m] = 21.8 [cm], which falls into pixel (21, 0)
		assertArrayEquals(intArray(21, 0), mazub.getLocation());
		
		mazub.startMoveLeft();
		mazub.advanceTime(0.1);
		
		// x_new [m] = .218 - .1045 = .1135 which falls into pixel 11
		assertArrayEquals(intArray(11, 0), mazub.getLocation());
		assertTrue(mazub.isRunningLeft);
		assertFalse(mazub.isRunningRight);
	}
	
	@Test
	public void endMoveLeftCorrectly(){
		Mazub mazub = new Mazub();
		mazub.startMoveRight();
		mazub.advanceTime(0.2);
		
		// x_new [m] = 0 + 1 [m/s] * 0.2 [s] + 1/2 0.9 [m/s^2] * (0.2 [s])^2 =
		// 0.218[m] = 21.8 [cm], which falls into pixel (21, 0)
		assertArrayEquals(intArray(21, 0), mazub.getLocation());
		
		mazub.startMoveLeft();
		mazub.advanceTime(0.1);
		
		// x_new [m] = .218 - .1045 = .1135 which falls into pixel 11
		assertArrayEquals(intArray(11, 0), mazub.getLocation());
		
		mazub.endMoveLeft();
		mazub.advanceTime(0.1);
		
		// the position should not change
		assertArrayEquals(intArray(11, 0), mazub.getLocation());
		assertFalse(mazub.isRunningLeft);
		assertTrue(mazub.getXSpeed() == 0);
		assertTrue(mazub.getXAccel() == 0);
	}
	
	@Test
	public void jumpCorrectly(){ // tests startJump, ySpeedFormula, yPosFormula, yMovementUpdate, yPosUpdate, setYSpeed, getYSpeed
		Mazub mazub = new Mazub();
		mazub.startJump();
		mazub.advanceTime(0.1);
		
		// y_new [m] = 0 + 8 [m/s] * 0.1 [s] - 1/2 * 10 [m/s^2] * (0.1 [s])^2 = 0.75 which falls in pixels (0, 75)
		assertArrayEquals(intArray(0, 75), mazub.getLocation());
		assertTrue(mazub.isFalling);
	}
	
	@Test
	public void endJumpCorrectly(){
		Mazub mazub = new Mazub();
		mazub.startJump();
		mazub.advanceTime(0.1);
		
		// y_new [m] = 0 + 8 [m/s] * 0.1 [s] - 1/2 * 10 [m/s^2] * (0.1 [s])^2 = 0.75 which falls in pixels (0, 75)
		assertArrayEquals(intArray(0, 75), mazub.getLocation());
		assertTrue(mazub.isFalling);
		
		mazub.endJump();
		mazub.advanceTime(0.1);
		
		// y_new [m] = .75 [m] - 1/2 * 10 [m/s^2] * (0.1 [s])^2 = 0.7 which falls in pixel (0, 70)
		assertArrayEquals(intArray(0, 70), mazub.getLocation());
		assertTrue(mazub.isFalling);
	}
	
	@Test
	public void stopFallingAfterJump(){
		Mazub mazub = new Mazub ();
		mazub.startJump();
		for (int i = 0; i < 7; i++) {
			mazub.advanceTime(1/5);
		}
		
		// after 8/5 seconds Mazub should be on the ground again
		// y_new [m] = 8 [m/s] * 8/5 [s] - 1/2 * 10 [m/s^2] * (8/5 [s])^2 = 0
		assertArrayEquals(intArray(0, 0), mazub.getLocation());
		assertFalse(mazub.isFalling);
	}
	
	@Test
	public void startDuckingCorrectly(){
		Mazub mazub = new Mazub();
		mazub.startDuck();
		
		assertTrue(mazub.isDucking);
	}
	
	@Test
	public void moveWhenDucking(){
		Mazub mazub = new Mazub();
		mazub.startDuck();
		mazub.startMoveRight();
		mazub.advanceTime(0.1);
		
		// x_new [m] = 1*.1 = 0.1 which falls into pixel (20, 0);
		assertArrayEquals(intArray(10, 0), mazub.getLocation());
	}
	
	
	@Test
	public void refreshTimeForSprite(){
		Mazub mazub = new Mazub();
		Sprite old = mazub.getCurrentSprite();
		
		//Mazub's sprite should only update every .075 seconds or more
		mazub.startMoveRight();
		mazub.advanceTime(.07);
		assertTrue(old == mazub.getCurrentSprite());
		
		mazub.advanceTime(.005);;
		assertFalse(old == mazub.getCurrentSprite());
	}
	
	@Test
	public void spriteSize(){
		Mazub mazub = new Mazub();

		//Mazub starts of as idle, this sprite has a size of 66 * 92 pixels
		assertArrayEquals(intArray(66, 92), mazub.getSize());
		
		mazub.startMoveRight();
		mazub.advanceTime(0.075);
		
		
		// after 0.075 seconds the first sprite of the walking animation is the current sprite
		// this sprite has a size of 72 * 97 pixels
		System.out.println(mazub.getCurrentSprite());
		assertArrayEquals(intArray(72, 97), mazub.getSize());
	}
	
	@Test 
	public void idleWhenIdle(){
		Mazub mazub = new Mazub();
		
		// Mazub starts of as idle
		assertTrue(mazub.idle);
		
		mazub.startMoveRight();
		mazub.advanceTime(.1);
		
		// while performing a horizontal movement Mazub is not idle
		assertFalse(mazub.idle);
		
		mazub.endMoveRight();
		for (int i = 0; i < 11; i++)
			mazub.advanceTime(.1);
		
		// after 1 second or more of no horizontal movement Mazub is idle again
		assertTrue(mazub.idle);
		
	}
}