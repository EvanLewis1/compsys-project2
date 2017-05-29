package mycontroller;

public class LavaHandler implements TrapHandler{

	public static void update(MyAIController controller, float delta) {
		controller.applyForwardAcceleration();
		
	}

}
