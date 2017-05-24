pragma solidity ^0.4.0;


contract SmartContract {
    address artist;
    uint value;
    address buyer;
    enum State {Created, Confirmed, Disabled}
    State state;

    function SmartContract() payable{
        artist = msg.sender;
        value = msg.value/2;    //price
    }

    function buySong() payable{
        if(state != State.Created) throw;
        if(msg.value != 2*value) throw; //value sent not enough send back to buyer
        buyer = msg.sender;
        state  = State.Confirmed;
    }
    function confirmPurchase(){
        if(msg.sender != buyer) throw;  //the one buying the song confirms purchase
        if(!buyer.send(value)) throw;
        if(!artist.send(this.balance)) throw;
        state = State.Disabled;
    }

}
