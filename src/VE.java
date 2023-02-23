import jade.core.Agent;


@SuppressWarnings("serial")
public class VE extends Agent{ 
	
	private int tempo_restante = 10;
	private int energiaNecessaria = 10;
	
	protected void setup() {
		addBehaviour(new VErequisitaEnergia(this));
	}

	public int getEnergiaNecessaria() {
		return energiaNecessaria;
	}

	public void setEnergiaNecessaria(int energiaNecessaria) {
		this.energiaNecessaria = energiaNecessaria;
	}
	public int getTempo_restante() {
		return tempo_restante;
	}

	public void setTempo_restante(int tempo_restante) {
		this.tempo_restante = tempo_restante;
	}
}
