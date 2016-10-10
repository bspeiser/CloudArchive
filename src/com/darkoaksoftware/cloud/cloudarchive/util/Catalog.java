package com.darkoaksoftware.cloud.cloudarchive.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.UUID;

public class Catalog implements Serializable{
	private HashMap<String,File> hmFiles;
	private HashMap hmHashes;
	private String filename;
	public Catalog() {
		init();
	}
	private void init(){
		hmFiles = new HashMap<String,File>();
		hmHashes = new HashMap();
	}
	public boolean inCatalog(File f){
		String hash = getID(f.getName()+f.lastModified()); 
		if (null==hmHashes.get(hash))return false;
		return true;
	}
	public void add(File f){
		hmFiles.put(f.getName(),f);
		String hash = getID(f.getName()+f.lastModified()); 
		hmHashes.put(hash,hash);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getID(String msg) {
		 String id = "";
		    try {
		    	MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			    crypt.reset();
				crypt.update(msg.getBytes("UTF-8"));
				id =  new BigInteger(1, crypt.digest()).toString(16);
			} catch (Exception e) {
				return UUID.randomUUID().toString();
			}
		    return id;
	}
	public static Catalog load(String fname) throws Exception{
		Catalog catalog;
		try {
			File file = new File(fname);
			
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream(f);
			catalog = (Catalog) s.readObject();
			s.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			catalog=new Catalog();
		} 
		catalog.setFileName(fname);

		return catalog;
	}
	private void setFileName(String fname) {
		filename= fname;
		
	}
	public void store(){
		try {
			File file = new File(filename);  
			FileOutputStream f = new FileOutputStream(file);  
			ObjectOutputStream s = new ObjectOutputStream(f);          
			s.writeObject(this);		
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
