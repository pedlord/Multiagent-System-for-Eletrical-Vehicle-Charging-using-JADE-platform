

public enum protocoloAgregador {
	REQUISITA_ENERGIA ("veiculo pediu recarga", 0),
	
	INFORMA_TEMPO_VE ("tempo restante do veiculo", 1),
	
	REQUISITA_ENERGIA_MERCADO("agregador vai contratar energia" , 2),
	
	ENERGIA_MERCADO("Energia contratada", 3);
	
	private final String protocolMessage;
	private final Integer code;
	
	private protocoloAgregador(String protocolMessage, Integer code) {
		this.protocolMessage = protocolMessage;
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}
	
	public static Integer getCodeFromString(String protocolMessage) {
		for(protocoloAgregador p : protocoloAgregador.values()) {
			if (p.toString().contentEquals(protocolMessage)) {
				return p.getCode();
			}
		}
		return -1;
	}
	
	public String toString() {
		return protocolMessage;
	}
}
