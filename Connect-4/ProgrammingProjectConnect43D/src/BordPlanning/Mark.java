package BordPlanning;


public enum Mark {

	EMPTY, XX, OO;

    /*@
       ensures this == Mark.XX ==> \result == Mark.OO;
       ensures this == Mark.OO ==> \result == Mark.XX;
       ensures this == Mark.EMPTY ==> \result == Mark.EMPTY;
     */
    /**
     * Returns the other mark.
     * 
     * @return the other mark is this mark is not EMPTY or EMPTY
     */
    public Mark other() {
        if (this == XX) {
            return OO;
        } else if (this == OO) {
            return XX;
        } else {
            return EMPTY;
        }
    }
    
    
    // in this too string we can add the user ID's 
    @Override
    public String toString() {
    	switch (this) {
    		case XX:
    			return "X";
    		case OO:
    			return "O";
    		default:
    			return "-";
    	}
    }
}
