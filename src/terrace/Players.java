package terrace;

import terrace.ai.AI;

public class Players {
	
	public Player newAIPlayer(){
		return new AI();
	}
	
	public Player newLocalPlayer(){
		return new LocalPlayer();
	}

}
