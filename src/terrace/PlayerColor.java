package terrace;

import java.awt.Color;

public enum PlayerColor {
	BLUE {
		@Override
		public Color toColor() {
			return new Color(51, 255, 204);
		}
	},
	PINK {
		@Override
		public Color toColor() {
			return new Color(255, 51, 102);
		}
	},
	YELLOW {
		@Override
		public Color toColor() {
			return new Color(245, 184, 0);
		}
	},
	GREEN {
		@Override
		public Color toColor() {
			return new Color(38, 153, 0);
		}
	};
	
	public abstract Color toColor();
}
