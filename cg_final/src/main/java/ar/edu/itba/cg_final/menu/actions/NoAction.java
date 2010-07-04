package ar.edu.itba.cg_final.menu.actions;

public class NoAction implements IAction {

	static private NoAction na;
	
	private NoAction(){};
	
	public static NoAction getInstance(){
		if ( na == null )
			na = new NoAction();
		return na;
	}
	
	public void performAction() {}

}
