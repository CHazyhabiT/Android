package com.chester.android.crystalball;

import java.util.Random;

public class CrystalBall {
	// Member variables (properties about the object)
	public String[] mAnswers;
	
	public CrystalBall(String[] answers) {
		mAnswers = answers;
	}
	
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
