package com.example.firebasechatapp;

public class Message {
    private String text,name,ImageUrl,sender,recipient;
    private boolean isMyMessage;
    public Message(String text, String name, String imageUrl,String recipient, String sender){
        this.ImageUrl = imageUrl;
        this.text = text;
        this.name = name;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Message(String text, String name, String imageUrl, boolean isMyMessage){
        this.ImageUrl = imageUrl;
        this.text = text;
        this.name = name;
        this.isMyMessage = isMyMessage;
    }

    public Message(String text, String name, String imageUrl){
        this.ImageUrl = imageUrl;
        this.text = text;
        this.name = name;
    }
    public Message(String imageUrl, String name){
        this.ImageUrl = imageUrl;
        this.name = name;
    }
    public Message(){

    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getText() {
        return text;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setText(String text) {
        this.text = text;
    }
}
