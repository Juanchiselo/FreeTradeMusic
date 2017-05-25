package FreeTradeMusic;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

import static org.web3j.tx.ManagedTransaction.GAS_PRICE;
import static org.web3j.tx.Transfer.GAS_LIMIT;

//On Command line: geth --rpcapi personal,db,eth,net,web3 --rpc --testnet

public class Blockchain extends Thread
{

    public Blockchain()
    {
        super("Blockchain Thread");
    }

    public void start()
    {
        //starting geth with command line
        Runtime rt = Runtime.getRuntime();
        try{Process pr = rt.exec("geth --rpcapi personal,db,eth,net,web3 --rpc --testnet");}
        catch(IOException e) {System.out.println("Command Line Error");}
        try
        {
            System.out.println("Blockchain thread launched!");
            Web3j web3 = Web3j.build(new HttpService());
            Web3ClientVersion clientversion = web3.web3ClientVersion().sendAsync().get();
            System.out.println("Client is running version: " + clientversion.getWeb3ClientVersion());

            Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile"); //Password and source need to be changed

            //This is used to create and deploy smart contract
            /*YourSmartContract contract = YourSmartContract.deploy(
                    <web3j>, <credentials>,
                    GAS_PRICE, GAS_LIMIT,
                    <initialEtherValue>,
                    <param1>, ..., <paramN>).get();  // constructor params*/

            //This is used for existing smart contract
            /*YourSmartContract contract = YourSmartContract.load(
                    "0x<address>", <web3j>, <credentials>, GAS_PRICE, GAS_LIMIT);*/

            //This is used to transact with smart contract
            /*TransactionReceipt transactionReceipt = contract.someMethod(
                    new Type(...),
             ...).get();*/

            //This is how to call a smart contract
            /*Type result = contract.someMethod(new Type(...), ...).get();*/
        }
        catch(Exception e)
        {
            System.err.println("ERROR: " + e.getMessage() + ".");
        }
    }
}
