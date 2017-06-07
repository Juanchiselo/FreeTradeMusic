pragma solidity ^0.4.0;


contract SmartContract {
    address artist;
    uint valueSong;
    uint valueAlbum;
    uint valueSongTotal;
    address buyer;
    enum State {Created, Confirmed, Disabled}
    State state;

    function SmartContract() payable{
        artist = msg.sender;
        valueSong = msg.value/2;    //price
        valueSongTotal += valueSong;
        valueAlbum = valueSongTotal - (valueSongTotal/5);
    }

    function buySong() payable{
        if(state != State.Created) throw;
        if(msg.value != 2*valueSong) throw; //value sent not enough send back to buyer
        buyer = msg.sender;
        state  = State.Confirmed;
    }
    function buyAlbum() payable{
        if(state != State.Created) throw;
        if(msg.value != valueAlbum) throw;
        buyer = msg.sender;
        state = State.Confirmed;
    }
    function confirmPurchase(){
        if(msg.sender != buyer) throw;  //the one buying the song confirms purchase
        if(!buyer.send(valueSong)) throw;
        if(!artist.send(this.balance)) throw;
        state = State.Disabled;
    }

}
