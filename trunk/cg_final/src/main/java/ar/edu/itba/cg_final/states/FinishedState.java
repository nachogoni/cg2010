package ar.edu.itba.cg_final.states;


public class FinishedState extends RallyGameState {
	
	public FinishedState() {
		this.setName("FinishedGame");
		stateNode.setName(this.getName());
	}

	@Override
	public void activated() {
		// Agregamos el stateNode al rootNode
		stateNode.setName(this.getName());
		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void deactivated() {
		rootNode.detachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float arg0) {
		// TODO Auto-generated method stub
		// Ir al menu?
	}

}
