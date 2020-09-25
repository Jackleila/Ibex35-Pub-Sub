import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;


import java.util.*;

public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

   private Vector clientList;
   private ArrayList<ArrayList<Object>> arrayServer;
   
   public CallbackServerImpl() throws RemoteException {
      super( );
      clientList = new Vector();
	  arrayServer = new ArrayList<>();
   }
	
  public String sayHello( )   
    throws java.rmi.RemoteException {
      return("Hola");
  }

  public synchronized void registerForCallback(
    CallbackClientInterface callbackClientObject, String empresa, String valor, String tipo)
    throws java.rmi.RemoteException{
      if (!(clientList.contains(callbackClientObject))) {
         clientList.addElement(callbackClientObject);
		 ArrayList<Object> array = new ArrayList<>();
		 array.add(callbackClientObject);
		 array.add(empresa);
		 array.add(valor);
		 array.add(tipo);
		 arrayServer.add(array);
      System.out.println("Nuevo cliente registrado ");
      
    }
  }  


  public synchronized void unregisterForCallback(
    CallbackClientInterface callbackClientObject) 
    throws java.rmi.RemoteException{
    if (clientList.removeElement(callbackClientObject)) {
      System.out.println("Unregistered client ");
    } else {
       System.out.println("unregister: clientwasn't registered.");
    }
  } 

  //Función para realizar los callback (lanzar las alertas)
  public synchronized void doCallbacks( ) throws java.rmi.RemoteException{

	//Obtener los datos
	Map<String, Double> datos = getData();
    System.out.println("---- Ejecucion de callbacks ----");
	ArrayList<ArrayList<Object>> clientsToRemove = new ArrayList<>();
    for (int i = 0; i < arrayServer.size(); i++){
      System.out.println("Ejecutando la alerta: "+ i + "\n");    
	  
      //CallbackClientInterface nextClient = (CallbackClientInterface)clientList.elementAt(i);
	  System.out.println(arrayServer.get(i).get(3));
	  System.out.println(arrayServer.get(i).get(2));
	  if(arrayServer.get(i).get(3).equals("venta")){
		  if(datos.get(arrayServer.get(i).get(1)) > Double.parseDouble((String)arrayServer.get(i).get(2))){
			CallbackClientInterface nextClient = (CallbackClientInterface) arrayServer.get(i).get(0);
			nextClient.notifyMe("Alerta de venta: " + arrayServer.get(i).get(1));
			clientsToRemove.add(arrayServer.get(i));
		  }
		  
	  }else{
		  if(datos.get(arrayServer.get(i).get(1)) <  Double.parseDouble((String)arrayServer.get(i).get(2))){
			CallbackClientInterface nextClient = (CallbackClientInterface) arrayServer.get(i).get(0);
			nextClient.notifyMe("Alerta de compra: " +arrayServer.get(i).get(1));
			clientsToRemove.add(arrayServer.get(i));
		  }
	  }
	 
		  
	  }
		 for(ArrayList<Object> cliente : clientsToRemove){
				  arrayServer.remove(cliente);
    }
 
  } 

  //Función para obtener los datos de la bolsa
  public Map<String, Double> getData()  throws java.rmi.RemoteException{
	  Map<String, Double> newDic = new HashMap<String, Double>() {};
	  try{
        
        Document doc = Jsoup.connect("http://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice").get();
        ArrayList<String> downServers = new ArrayList<>();
        Element table = doc.select("table#ctl00_Contenido_tblAcciones").get(0); //seleccionamos la tabla 
        Elements rows = table.select("tr");

        String nString = "";
        for (int i = 1; i < rows.size(); i++) { //Saltamos la primera columna
            Element row = rows.get(i);
            Elements cols = row.select("td");
            nString = cols.get(1).text();
            nString = nString.replaceAll(",",".");
            newDic.put(cols.get(0).text(), Double.parseDouble(nString));
        }
			
	  }catch(Exception ex){
		  ex.printStackTrace();	  
	  }  
	  return newDic;
  } 

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}// end CallbackServerImpl class   
