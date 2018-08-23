package JoS.JAVAProjects;

import java.util.*;

public class Hashmap {
	// Create a hash map
	public  HashMap <String, String> hm = new HashMap <String, String>();
	 
	 
	public  void AssignHashValueToHashKey(String key, String keyvalue) {
		try{
			 //DebugInterface di=new DebugInterface();
			// Put elements to the map
			hm.put(key, keyvalue);
	
			// Get an entry
			String assignedValue = hm.get(key);
			if (assignedValue == keyvalue){
				/*di.Debug ("The value has been assigned to the key");
				di.Debug ("Key: '" + key +"'. It's value: '"+ assignedValue + "'"); 
				di.Debug ("Size of Map: " + hm.size());*/
				}
			}catch (Exception e) {
		    e.printStackTrace();
			}
		}
	public  String GetHashValueFromHashKey(String key) {
		String assignedValue = null;
		try{
			// DebugInterface di=new DebugInterface();
			// Get an entry
			assignedValue = hm.get(key);
			//di.Debug("Key: '" + key +"'. It's value: '"+ assignedValue + "'");
			
			}catch (Exception e) {
		    e.printStackTrace();
			}
		return assignedValue;
		}
	//public static void ClearHashMap(HashMap<String, String> hm) {
	public  void ClearHashMap() {
		try{
			/* DebugInterface di=new DebugInterface();
			di.Debug("Size of Map: " + hm.size());*/
			hm.clear(); //clears Hashmap , removes all element
			//di.Debug("Size of Map: " + hm.size()); 
			}catch (Exception e) {
		    e.printStackTrace();
			}
		}
	public  boolean DoesHashKeyExist(String sKey) {
		//DebugInterface di=new DebugInterface();
		try{
			boolean bRet=false;
			
			bRet=hm.containsKey(sKey);
			if (bRet){
				//di.Debug ("Key - "+sKey+" exists in hashmap " );
				return true;
			}else{
				//di.Debug ("Key - "+sKey+" doesn't exist in hashmap " );
				return false;
			}

			}catch (Exception e) {
				//di.Debug ("Key - "+sKey+" does not exist in hashmap " );
				return false;
			}
		}
}