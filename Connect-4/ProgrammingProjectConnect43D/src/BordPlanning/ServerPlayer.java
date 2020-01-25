package BordPlanning;


public class ServerPlayer extends Player {
	
	private int[] nextMove;

	public ServerPlayer(String ID, Mark mark) {
		super(ID, mark);
	}
	
	
	@Override
	public int[] determineMove(Board board) {
		return nextMove;
	}
	
	public void setNextMove(int[] move) {
		nextMove = move;
	}

}
