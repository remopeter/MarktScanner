package ch.bpeter.marktscanner.datenbank.tabellen;

public class PreisVO {
	private String preis;
	private String artikel_id;
	private String haendler_id;
	
	public String getPreis() {
		return preis;
	}
	public void setPreis(String preis) {
		this.preis = preis;
	}
	public String getArtikel_id() {
		return artikel_id;
	}
	public void setArtikel_id(String artikelId) {
		artikel_id = artikelId;
	}
	public String getHaendler_id() {
		return haendler_id;
	}
	public void setHaendler_id(String haendlerId) {
		haendler_id = haendlerId;
	}
}
