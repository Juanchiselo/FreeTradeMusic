package FreeTradeMusic;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

//On Command line: geth --rpcapi personal,db,eth,net,web3 --rpc --testnet

public class Blockchain extends Thread
{

    public Blockchain()
    {
        super("Blockchain Thread");
    }

    public void start()
    {
        try
        {
            System.out.println("Blockchain thread launched!");
            Web3j web3 = Web3j.build(new HttpService());
            Web3ClientVersion clientversion = web3.web3ClientVersion().sendAsync().get();
            System.out.println("Client is running version: " + clientversion.getWeb3ClientVersion());
        }
        catch(Exception e)
        {
            System.err.println("ERROR: " + e.getMessage() + ".");
        }
    }
}
