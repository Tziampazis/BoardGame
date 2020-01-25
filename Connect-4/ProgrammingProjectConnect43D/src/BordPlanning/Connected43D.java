package BordPlanning;

public class Connected43D {

	public static void main(String[] args) {

		
		if (args.length >= 2) {
        	Player s1 = determinePlayer(args[0], Mark.XX);
            Player s2 = determinePlayer(args[1], Mark.OO);
        	Game2 game = new Game2(s1, s2, 4,4,4,4);
            game.start();
        }else{
        	System.out.println("Too few arguments...");
        	}
		
	}
		
		public static Player determinePlayer(String arg,Mark mark){//it can also be done by if statements
			String args = arg;
			if (args.equals("-N")){
				return new ComputerPlayer(mark , new NaiveStrategy());
			}else if(args.equals("-S")){
				return new ComputerPlayer(mark , new SmartStrategy());
			}else{
			return new HumanPlayer(arg, mark);
			}
		}
	}


