import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class CallbackServer  {
  public static void main(String args[]) {
    InputStreamReader is = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(is);
    String portNum, registryURL;
    try{     

	  //Obtención del puerto de conexión
      System.out.println("Introduzca el puerto de registro:");
      portNum = (br.readLine()).trim();
      int RMIPortNum = Integer.parseInt(portNum);
	  
	  //Comenzar registro
      startRegistry(RMIPortNum);
      CallbackServerImpl exportedObj = new CallbackServerImpl();
      registryURL = "rmi://localhost:" + portNum + "/callback";
      Naming.rebind(registryURL, exportedObj);
      System.out.println("Servidor preparado.");
	  
	  //Lanzamos las alertas cada minuto
	  Timer timer = new Timer();
		timer.schedule( new TimerTask() {
			public void run() {
				try {
			   exportedObj.doCallbacks(); 
			}
    catch (RemoteException e) { 
		e.printStackTrace();
	}
			}
		 }, 0, 60*1000);
    }
    catch (Exception re) {
      System.out.println(
        "Excepcion en HelloServer.main: " + re);
    }
  }

  //Método para comenzar el registro
  private static void startRegistry(int RMIPortNum)
    throws RemoteException{
    try {
      Registry registry = LocateRegistry.getRegistry(RMIPortNum);
      registry.list( );  
    }
    catch (RemoteException e) { 
      //Si el registro no existe
      Registry registry = LocateRegistry.createRegistry(RMIPortNum);
    }
  }
}
