package oortcloud.network;

public class PacketGeneralClient extends PacketBasicClient{
	
	/**
	 * 0 : set player management ['p0' 'p1' '' 't1' 'p2' p3' '' ... ] 
	 * 1 : set player ['p' 't']
	 */
	
	public PacketGeneralClient() {
		super();
	}
	
	public PacketGeneralClient(int index) {
		super(index);
	}
}
