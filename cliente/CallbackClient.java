import java.io.*;
import java.rmi.*;
import java.util.*;

public class CallbackClient {

  public static void main(String args[]) {
    try {
	  //Obtener puerto y host
      int RMIPort;         
      String hostName;
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      System.out.println("Introduzca el nombre del host:");
      hostName = br.readLine();
      System.out.println("Introduzca el puerto:");
      String portNum = br.readLine();
      RMIPort = Integer.parseInt(portNum); 

	  //Registro
      String registryURL = "rmi://localhost:" + portNum + "/callback";  
      CallbackServerInterface h = (CallbackServerInterface)Naming.lookup(registryURL);
      System.out.println("El servidor dice: " + h.sayHello());
	  
	  while(true){
		  System.out.println("Introduzca la empresa que desea consultar:");
		  String empresa = br.readLine();
		  System.out.println("Introduzca el umbral:");
		  String valor = br.readLine();
		  System.out.println("Tipo de alerta: (venta o compra)");
		  String tipo = br.readLine();
		  CallbackClientInterface callbackObj = new CallbackClientImpl();
		  h.registerForCallback(callbackObj, empresa, valor, tipo);
		  System.out.println("Registrado para el callback.");
	  }
    }
    catch (Exception e) {
      System.out.println(
        "Excepcion en CallbackClient: " + e);
    }
  }
}
