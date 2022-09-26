package com.synchrofield.floaxial.server.droplet;

import java.util.ArrayList;

public class DropletMoveList extends ArrayList<DropletMove>{

	private static final long serialVersionUID = 1L;
	
	public static DropletMoveList of( ) {
		
		return new DropletMoveList();
	}
}
