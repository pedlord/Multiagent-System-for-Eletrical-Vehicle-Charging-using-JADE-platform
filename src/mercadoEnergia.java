
import jade.core.Agent;

@SuppressWarnings("serial")
public class mercadoEnergia extends Agent {
	protected void setup() {
		addBehaviour(new mercadoAcao(this));
	}
}
