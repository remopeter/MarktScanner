package ch.bpeter.marktscanner.datenbank.tabellen;

import java.util.ArrayList;

public class T_ARTIKEL {
	private int artikel_id;
	private String name;
	private String barcode;
	private String foto;
	
	
	// Suchmethoden
	public T_ARTIKEL findById(int artikel_id){
		T_ARTIKEL t_artikel = new T_ARTIKEL();
		return t_artikel;
	}
	
	public ArrayList<T_ARTIKEL> findByName(String Name){
		ArrayList<T_ARTIKEL> list=new ArrayList<T_ARTIKEL>();
		return list;
	}
	
	public ArrayList<T_ARTIKEL> findByBarcode(String Barcode){
		ArrayList<T_ARTIKEL> list=new ArrayList<T_ARTIKEL>();
		return list;
	}
	
	// Getter- und Setter- Methoden
	public int getArtikel_id() {
		return this.artikel_id;
	}
	public void setArtikel_id(int artikelId) {
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
}
