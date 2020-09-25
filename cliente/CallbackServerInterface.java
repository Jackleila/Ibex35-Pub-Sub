import java.rmi.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public interface CallbackServerInterface extends Remote {

  public String sayHello( ) throws java.rmi.RemoteException;


  public void registerForCallback(CallbackClientInterface callbackClientObject, String empresa, String valor, String tipo) throws java.rmi.RemoteException;

  public void unregisterForCallback(
    CallbackClientInterface callbackClientObject)
    throws java.rmi.RemoteException;
	
	
	public Map<String, Double> getData()  throws java.rmi.RemoteException;

}
