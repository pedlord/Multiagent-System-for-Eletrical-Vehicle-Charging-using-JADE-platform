import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class mercadoAcao extends Behaviour {
	public mercadoAcao (Agent a) {
		super(a);
	}
	public void action() {
		try {
			ACLMessage contrato = myAgent.receive(MessageTemplate.MatchProtocol(protocoloAgregador.REQUISITA_ENERGIA_MERCADO.toString()));
			
			double pot = Integer.parseInt(contrato.getContent());
			
			ACLMessage energia = contrato.createReply();
			energia.setProtocol(protocoloAgregador.ENERGIA_MERCADO.toString());
			energia.addReceiver(new AID("agregador", AID.ISLOCALNAME));
			energia.setContent(String.valueOf(pot));
			myAgent.send(energia);
		} catch (Exception e) {
			block();
		}
	}
	public boolean done() {
		return true;
	}
}
