package ch.bpeter.marktscanner.datenbank.tabellen;

public class ArtikelVO {
	private String artikel_id;
	private String name;
	private String barcode;
	private String foto;
	private String marke_id;
	
	// Getter- und Setter- Methoden
	public String getArtikel_id() {
		return this.artikel_id;
	}
	public void setArtikel_id(String artikelId) {
		this.artikel_id = artikelId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getMarke_id() {
		return marke_id;
	}
	public void setMarke_id(String markeId) {
		marke_id = markeId;
	}
}
