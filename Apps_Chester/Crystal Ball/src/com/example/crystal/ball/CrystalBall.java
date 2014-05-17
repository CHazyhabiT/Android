package com.example.crystal.ball;

import java.util.Random;

public class CrystalBall {
	// Member variables (properties about the object)
	public String[] mAnswers = { 
			"MJ", 
			"Cha for Tea",
			"Yusoken", 
			"In and Out",
			"Subway", 
			"Gogi",
			"Grade 3 Class 2", 
			"Panda",
			"Wendeys",
			"Teriyaki",
			"Sandwich",
			"Chipotile"};
	
	// Methods (abilities: things the object can do)
	public String getAnAnswer() { 

		String answer = "";
		
		// Randomly select one of three answers: Yes, No, or Maybe
		Random randomGenerator = new Random(); // Construct a new Random number generator
		int randomNumber = randomGenerator.nextInt(mAnswers.length);
		
		answer = mAnswers[randomNumber];
		
		return answer;
	}
}
