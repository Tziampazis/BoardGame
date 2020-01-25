package BordPlanning;



public class ComputerPlayer extends Player {
	
	public Strategy strategy;
	
	public ComputerPlayer(Mark mark, Strategy strategy ) {
		super(strategy.getName() + "-computer-" + mark.toString(), mark);//mark.toString for the representation of the player's mark
		this.strategy = strategy; 
	}
	
	public ComputerPlayer (Mark mark){
		this(mark,new NaiveStrategy());
	}

	@Override
	public int[] determineMove(Board board) {
		return strategy.determineMove(board, this.getMark());
	}

}
