package animation;

import graphics.Window;
import objects.Circle;
import objects.Device;

public class Animator {
	
	private static float LOCATOR_ANIMATION_TIME = 2f;
	
	private static float animationTime = 0;
	
	/*
	 * Method to change the update time of the animator
	 */
	
	public static void update(Device device)
	{
		animationTime += Window.getDeltaTime();
		
		if(animationTime > LOCATOR_ANIMATION_TIME)
		{
			animationTime = 0;
			// Reset each locator's radius
			device.getLocator1().setRadius(0.05f);
			device.getLocator2().setRadius(0.05f);
			device.getLocator3().setRadius(0.05f);
			
			// Reset each locator's opacity to 1
			device.getLocator1().setOpacity(1);
			device.getLocator2().setOpacity(1);
			device.getLocator3().setOpacity(1);
		}
	}
	
	/*
	 * Method to animate circles acting as device locators
	 * Method will determine the difference between the current radius
	 * And it's destination and linearly interpolate between them
	 */
	public static void animateLocator(Circle locator)	{
		
		/*
		 * Determine the speed and change in radius
		 * Alter radius accordingly
		 */
		float startRadius = 0.05f;
		float destinationRadius = locator.getMaxRadius();
		
		float diffRadius = destinationRadius - startRadius;
		
		float velRadius = diffRadius / LOCATOR_ANIMATION_TIME;
		
		float deltaRadius = velRadius * Window.getDeltaTime();
		
		locator.increaseRadius(deltaRadius);
		
		/*
		 * Determine speed and change in opacity
		 * Alter opacity accordingly
		 */
		
		float startOpacity = 1f;
		float destinationOpacity = 0f;
		
		float diffOpacity = destinationOpacity - startOpacity;
		
		float deltaOpacity = (diffOpacity / LOCATOR_ANIMATION_TIME) * Window.getDeltaTime();
		locator.increaseOpacity(deltaOpacity);
	}

}
