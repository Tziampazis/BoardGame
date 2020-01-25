package BordPlanning;

import java.util.Random;

public class NaiveStrategy implements Strategy {

	public static Random random = new Random();
	
	@Override
	public String getName() {
		return "Naive";
	}

	@Override
	public int[] determineMove(Board b, Mark m) {
		int[] rand = new int[2];
		boolean con = true;
		while(con){
			int rand1 = random.nextInt(b.dimX);//from 0...DIM-1
			int rand2 = random.nextInt(b.dimY);//from 0...DIM-1
			if (b.hasEmptyField(rand1,rand2)){
				rand[0] = rand1; 
				rand[1] = rand2; 
				con = false;
			}else{
				con = true;
			}
		}
		return rand;
	}
}
