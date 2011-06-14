package ch.bpeter.marktscanner.datenbank.tabellen;

public class HaendlerVO {
	private String haendler_id="";
	private String haendlername="";
	
	public String getHaendler_id() {
		return haendler_id;
	}

	public void setHaendler_id(String haendlerId) {
		haendler_id = haendlerId;
	}

	public String getHaendlername() {
		return haendlername;
	}

	public void setHaendlername(String haendlername) {
		this.haendlername = haendlername;
	}
}
