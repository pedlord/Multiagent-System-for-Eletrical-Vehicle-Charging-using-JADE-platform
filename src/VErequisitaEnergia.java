import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;


@SuppressWarnings("serial")
public class VErequisitaEnergia extends Behaviour{
	
	public VErequisitaEnergia (Agent a) {
		super(a);
	}
	
	
	@Override
	public void action() {
		//System.out.println(String.valueOf(((VE) myAgent).getEnergiaNecessaria()));
		
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.setProtocol(protocoloAgregador.REQUISITA_ENERGIA.toString());
		message.addReceiver(new AID("agregador", AID.ISLOCALNAME));
		message.setContent(String.valueOf(((VE) myAgent).getEnergiaNecessaria()));
		myAgent.send(message);
		
		ACLMessage tempo = new ACLMessage(ACLMessage.INFORM);
		tempo.setProtocol(protocoloAgregador.INFORMA_TEMPO_VE.toString());
		tempo.addReceiver(new AID("agregador", AID.ISLOCALNAME));
		tempo.setContent(String.valueOf(((VE) myAgent).getTempo_restante()));
		myAgent.send(tempo);
		
	}
	
	public boolean done() {
		return true;
	}
}
