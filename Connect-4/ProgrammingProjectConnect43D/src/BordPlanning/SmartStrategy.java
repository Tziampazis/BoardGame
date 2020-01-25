package BordPlanning;

import java.util.Random;
 
public class SmartStrategy implements Strategy {
	
	public static Random random = new Random();
	public int[] move =new int[2];
	
	/**
	 * @return name of the class(name of strategy)
	 */
	/*@pure
	 */
	@Override
	public String getName() {
		return "SmartStrategy";
	}

	/**
	 * This method check first for possible opponent win and response by blocking it.
	 * If there is no possible winning then a check is made if the smart has any possible win in the board.
	 * Finally, if non of the above applies then smart makes a random move.
	 * @return move which depends on the check of the for loops
	 */
	/*@
	 */
	@Override
	public int[] determineMove(Board b, Mark m) {
		
		for(int rX = 0; rX<b.dimX;rX++){//check overall
			Board copy = new Board(); //copy of this game
			copy = b.deepCopy();
			if (copy.hasEmptyField(rX, rX) && copy.isField(rX, rX)){
				copy.setField(rX, rX, m.other());
				if(copy.isWinner(m.other())){
					move[0] = rX;
					move[1] = rX;
					//System.out.println(move[0]+ "other is winner Overall" + move[1]);
					return move;
				}
			}
		}
		
		for(int rX = 0; rX<b.dimX;rX++){//check for oponent X,Y,Z
			Board copy = new Board(); //copy of this game
			for(int rY = 0; rY<b.dimX;rY++){
					copy = b.deepCopy();
				if(copy.hasEmptyField(rX, rY) &&copy.isField(rX, rY)){
					copy.setField(b.dimZ-1,rY, rX, m.other());
					if( copy.isWinner(m.other())){
							move[0] = rX;
							move[1] = rY;
							//System.out.println(move[0]+ "other is winner XYZ" + move[1]);
							return move;
					}
				}
			}
		}
		
		for(int rX= 0; rX<b.dimZ;rX++){//works finaly
			for(int rZ= 0; rZ<b.dimZ;rZ++){//check for oponent Y,Z+
				Board copy = new Board(); //copy of this game
						copy = b.deepCopy();
						if (copy.hasEmptyField(rX, rZ) && copy.isField(rX, rZ)){
							copy.setField(rX, rZ, m.other());
							if(copy.isWinner(m.other())){
								move[0] = rX;
								move[1] = rZ;
								//System.out.println(move[0]+ "other is winner YZ+" + move[1]);
								return move;
							}
					}
				}
		}
		
		for(int rX = 0; rX<b.dimX;rX++){//check for oponent X,Y
			Board copy = new Board(); //copy of this game
			for(int rY = 0; rY<b.dimX;rY++){
				copy = b.deepCopy();
				if(copy.isField(rX, rY)&& copy.hasEmptyField(rX, rY)){
				copy.setField(rX, rY, m.other());
					if(copy.isWinner(m.other())){
						move[0] = rX;
						move[1] = rY;
						//System.out.println(move[0]+ "other is winner XY" + move[1]);
						return move;
					}
				}
			}
		}
		for(int rY =0; rY<b.dimY;rY++){//check for oponent Y,X
			Board copy = new Board(); //copy of this game
				for(int rX = 0; rX<b.dimX;rX++){
				copy = b.deepCopy();
				if(copy.isField(rX,rY ) && copy.hasEmptyField(rX, rY)){
				copy.setField(rX, rY, m.other());
					if(copy.isWinner(m.other())){
						move[0] = rX;
						move[1] = rY;
						//System.out.println(move[0]+ "other is winner YX" + move[1]);
						return move;
					}
				}
			}
		}
		//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		for(int rX = 0; rX<b.dimX;rX++){//check overall
			Board copy = new Board(); //copy of this game
			copy = b.deepCopy();
			if (copy.hasEmptyField(rX, rX) && copy.isField(rX, rX)){
				copy.setField(rX, rX, m);
				if(copy.isWinner(m)){
					move[0] = rX;
					move[1] = rX;
					//System.out.println(move[0]+ "other is winner Overall" + move[1]);
					return move;
				}
			}
		}
		
		for(int rX= 0; rX<b.dimZ;rX++){//works finaly
			for(int rZ= 0; rZ<b.dimZ;rZ++){//check for smart Y,Z+
				Board copy = new Board(); //copy of this game
						copy = b.deepCopy();
						if (copy.hasEmptyField(rX, rZ) && copy.isField(rX, rZ)){
							copy.setField(rX, rZ, m);
							if(copy.isWinner(m)){
								move[0] = rX;
								move[1] = rZ;
								//System.out.println(move[0]+ " is winner YZ+" + move[1]);
								return move;
							}
					}
				}
		}
		
		for(int rX = 0; rX<b.dimX;rX++){//check for smart X,Y,Z
			Board copy = new Board(); //copy of this game
			for(int rY = 0; rY<b.dimX;rY++){
				copy = b.deepCopy();
				if(copy.hasEmptyField(rX, rY) &&copy.isField(rX, rY)){
				copy.setField(b.dimZ-1,rY, rX, m);
					if( copy.isWinner(m)){
						move[0] = rX;
						move[1] = rY;
						//System.out.println(move[0]+ "is winner XYZ" + move[1]);
						return move;
					}
				}
			}
		}
		
		for(int rX = 0; rX<b.dimX;rX++){// check for smart X,Y
			Board copy = new Board(); //copy of this game
			for(int rY = 0; rY<b.dimX;rY++){
				copy = b.deepCopy();
				if(copy.isField(rX, rY) && copy.hasEmptyField(rX, rY)){
				copy.setField(rX, rY, m);
					if(copy.isWinner(m)){
						move[0] = rX;
						move[1] = rY;
						//System.out.println(move[0]+ " is winner XY" + move[1]);
						return move;
					}
				}
			}
		}
		
		
		//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		//move if none of above apply
		int rand1= random.nextInt(b.dimX);
		int rand2= random.nextInt(b.dimY);
		if (b.hasEmptyField(rand1, rand2) && b.isField(rand1, rand2)){
			//System.out.println("first" +rand1+ " "+rand2);
			move[0] = rand1;
			move[1] = rand2;
		}
		
		return move;
	}
}

