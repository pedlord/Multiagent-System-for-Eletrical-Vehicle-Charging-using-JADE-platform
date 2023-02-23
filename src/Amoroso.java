import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
//import jade.util.leap.ArrayList;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@SuppressWarnings("serial")
public class Amoroso extends CyclicBehaviour {
	
	public Amoroso (Agent a) {
		super(a);
	}
	
	private Integer state = 0;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void action() {
				
		double prioridade, ener_nece_interv = 1, pot_max = 1;
		int tempo_veiculo=1;
		ArrayList VEinfo = new ArrayList();

		switch(state) {
			case 0:
				int tama = myAgent.getQueueSize();
				if (tama==0 && ((AgenteAgregador) myAgent).ListaPriori.size() == 0) {
					try {
						ACLMessage veiculoReq =  myAgent.receive(MessageTemplate.MatchProtocol(protocoloAgregador.REQUISITA_ENERGIA.toString()));
					} catch (Exception e) {
						block();
					}
				}
			case 1:
					//repetir loop por numero de veiculos conectados
					int tam = myAgent.getQueueSize();
					System.out.println(tam);
					if (tam==0 && ((AgenteAgregador) myAgent).ListaPriori.size() > 0) {
						System.out.println(tam);
						System.out.println(((AgenteAgregador) myAgent).ListaPriori.size());
						System.out.println("chegou?");
						state = 2;
					}
					else {
						System.out.println("chegou? 2");
						try {
							System.out.println("chegou? 3");
							ArrayList VEi;
							for (int i = 0; i < tam/2; i++) {
								ACLMessage veiculoReq =  myAgent.receive(MessageTemplate.MatchProtocol(protocoloAgregador.REQUISITA_ENERGIA.toString()));
								ener_nece_interv = Integer.parseInt(veiculoReq.getContent());
							
								ACLMessage veiculoTemp = myAgent.receive(MessageTemplate.MatchProtocol(protocoloAgregador.INFORMA_TEMPO_VE.toString()));
								tempo_veiculo = Integer.parseInt(veiculoTemp.getContent());
								
								VEi = new ArrayList();
								VEi.add(veiculoReq.getSender());
								VEi.add(tempo_veiculo);
								VEi.add(ener_nece_interv);
								VEinfo.add(VEi);
								System.out.println(VEi);
							}
							tam = VEinfo.size();
							double ener = 0;
							for (int i = 0; i < tam; i++) {  //Seleciona o VE com maior energia para pedir no mercado
								if (ener < (double) ((ArrayList) VEinfo.get(i)).get(2)) {
									ener = (double) ((ArrayList) VEinfo.get(i)).get(2);
								}
							}
							ACLMessage mercado = new ACLMessage(ACLMessage.REQUEST);
							mercado.setProtocol(protocoloAgregador.REQUISITA_ENERGIA_MERCADO.toString());
							mercado.addReceiver(new AID("mercado", AID.ISLOCALNAME));
							mercado.setContent(String.valueOf(ener));
							myAgent.send(mercado);
						
							ACLMessage energiaContratada = myAgent.receive(MessageTemplate.MatchProtocol(protocoloAgregador.ENERGIA_MERCADO.toString()));
							pot_max = Integer.parseInt(energiaContratada.getContent());
							
							for (int i = 0; i < tam; i++) { //calcula prioridade e adiciona a prioridade a lista
								//prioridade = ener_nece_interv/(pot_max * tempo_veiculo);
								prioridade = (double) ((ArrayList) VEinfo.get(i)).get(2)/(pot_max * (double) ((ArrayList) VEinfo.get(i)).get(1));
								((ArrayList) VEinfo.get(i)).add(1, prioridade);
							}
							//ordena lista
							Collections.sort(VEinfo, new Comparator<List<Double>>() {
							    @Override
							    public int compare(List<Double> a, List<Double> b) {
							    	return Double.compare(a.get(1), b.get(1));
							    }
							});
							for (int i = 0; i < VEinfo.size(); i++) {
								((AgenteAgregador) myAgent).ListaPriori.add(VEinfo.get(i));
							}
							state = 2;
						} catch (Exception e) {
							block();
						}
					}
			case 2:
				System.out.println("VEinfo");
				System.out.println(VEinfo);
				System.out.println("listaprio");
				System.out.println(((AgenteAgregador) myAgent).ListaPriori);
				if (((AgenteAgregador) myAgent).ListaPriori.size() > 1) {
					ArrayList temp = new ArrayList();
					for (int i = 0; i < ((AgenteAgregador) myAgent).ListaPriori.size(); i++) {
						temp.add(((AgenteAgregador) myAgent).ListaPriori.get(i));
						((AgenteAgregador) myAgent).ListaPriori.remove(i);
					}
					
					 //ordena lista Novamente no proximo intervalo
					
						Collections.sort(temp, new Comparator<List<Double>>() {
						    @Override
						    public int compare(List<Double> a, List<Double> b) {
						    	return Double.compare(a.get(1), b.get(1));
						    }
						});
					for (int i = 0; i < VEinfo.size(); i++) {
						((AgenteAgregador) myAgent).ListaPriori.add(temp.get(i));
					}
				}
				state = 3;
			case 3:
				System.out.println(((AgenteAgregador) myAgent).ListaPriori);
				double carrega; //valor de carregamento no intervalo atual
				
				int NewTemp;  //guarda tempo atual pra diminuir depois na lista
				
				int a = ((AgenteAgregador) myAgent).ListaPriori.size();
				boolean sair = false;
				ArrayList ListaSaida = new ArrayList();
				
				for (int i = 0; i < a; i++) {
					carrega = (double) ((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).get(3);
					carrega = carrega - pot_max;
					if (carrega > 0) {   //se true entao veiculo nao foi carregado e deve atualizar valores na lista
						NewTemp = (int) ((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(0)).get(2) - 1;
						
						((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).remove(3); //remove energia pra substituir pela nova
						((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).remove(2); //remove tempo pra substituir pelo novo
						((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).add(NewTemp);
						((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).add(carrega);
					}
					else { //veiculo carrega e deve sair da lista
						ListaSaida.add((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i));
					}
				}
				if (sair == true) { //retirar da lista o veiculo carregado
					a = ListaSaida.size();
					for (int i = 0; i < a; i++) {
						ListaSaida.get(i);
						for (int j = 0; j < ((AgenteAgregador) myAgent).ListaPriori.size(); i++) {
							if (ListaSaida.get(i) == (ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(j)) {
								((AgenteAgregador) myAgent).ListaPriori.remove(j);
							}
						}
					}
				}
				System.out.println(((AgenteAgregador) myAgent).ListaPriori);
				state = 4;
			case 4:
				a = ((AgenteAgregador) myAgent).ListaPriori.size();
				for (int i = 0; i < a; i++) { //recalcula prioridade
					ener_nece_interv = (double) ((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).get(3);
					tempo_veiculo = (int) ((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).get(2);
					prioridade = ener_nece_interv/(pot_max * tempo_veiculo);
					((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).remove(1);
					((ArrayList) ((AgenteAgregador) myAgent).ListaPriori.get(i)).add(1, prioridade);
				}
		}
	}
}
