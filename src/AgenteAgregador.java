import jade.core.Agent;
import jade.util.leap.ArrayList;

@SuppressWarnings("serial")
public class AgenteAgregador extends Agent {
	
	ArrayList ListaPriori = new ArrayList();
	
	protected void setup() {
		addBehaviour(new Amoroso(this));
	}

}

