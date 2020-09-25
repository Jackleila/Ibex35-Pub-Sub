import java.rmi.*;
import java.rmi.server.*;

public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {

  
	//Constructor de la clase
    public CallbackClientImpl() throws RemoteException {
       super( );
	   
   }
	//Notificación de alerta
    public String notifyMe(String message){
       String returnMessage = "Recibida alerta: " + message;
       System.out.println(returnMessage);
       return returnMessage;
    }      

}
