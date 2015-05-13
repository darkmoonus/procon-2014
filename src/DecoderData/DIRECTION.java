package DecoderData;

public enum DIRECTION { 
	UP, DOWN, LEFT, RIGHT, NONE;
	
	public DIRECTION invert() {
		switch(this) {
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case NONE:
			return NONE;
		case RIGHT:
			return LEFT;
		case UP:
			return DOWN;
		default:
			return NONE;
		}
	}
	
	@Override
	public String toString() {
		switch(this) {
		case DOWN:
			return "D";
		case LEFT:
			return "L";
		case RIGHT:
			return "R";
		case UP:
			return "U";
		default:
			return "N";
		}
	}
}
