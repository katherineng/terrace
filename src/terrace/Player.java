package terrace;

import com.google.common.base.Optional;


public abstract class Player {
	PlayerColor _color;
	String _name;
	
	public Player(PlayerColor color) {
		_color = color;
		_name = "";
	}
	
	/**
	 * Gets a player's next move.
	 * 
	 * @param timeout The maximum number of seconds to block
	 * @return        The player's desired move, if any
	 */
	public abstract Optional<Move> getMove(int timeout);
	
	public PlayerColor getColor() {
		return _color;
	}
	
	public String getName() {
		return _name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (_color != other._color)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return _color.toString();
	}

	public void setName(String name) {
		_name = name;
	}
	
	/**
	 * Used to drop disconnecting client players.
	 * 
	 * @return Whether or not this player wants to forfeit.
	 */
	public boolean wantToForfeit() {
		return false;
	}
}
