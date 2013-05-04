package message;

import java.util.List;

public class Request {
	private List<String> _playerNames;
	
	public Request(List<String> playerNames) {
		_playerNames = playerNames;
	}
	
	public List<String> getPlayerNames(){
		return _playerNames;
	}
	@Override
	public String toString() {
		String s  = "";
		for(String name : _playerNames) {
			s += name + ", ";
		}
		return s.substring(0, s.length() - 2);
	}
}
