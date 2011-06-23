package ch.bpeter.marktscanner.interfaces;

import java.util.ArrayList;

public interface I_MarktScannerDAO {
	
	public ArrayList<?> findAll();
	
	public ArrayList<?> findByAttributes(String []attributes, String []values);
	
	public Object findById(String id);
	
	public Object save(Object obj);
	
	public boolean deleteById(String id);
}
