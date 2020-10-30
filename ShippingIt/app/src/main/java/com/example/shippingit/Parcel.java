package com.example.shippingit;

public class Parcel {
    private String Addressee;
    private String Recipient;
    private String Deliveryaddress;
    private String Pickupaddress;
    private String Day;

    public Parcel()
    {

    }

    public Parcel(String addressee, String recipient, String deliveryaddress, String pickupaddress, String day) {
        Addressee = addressee;
        Recipient = recipient;
        Deliveryaddress = deliveryaddress;
        Pickupaddress = pickupaddress;
        Day = day;
    }

    public String getAddressee() {
        return Addressee;
    }

    public void setAddressee(String addressee) {
        Addressee = addressee;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public String getDeliveryaddress() {
        return Deliveryaddress;
    }

    public void setDeliveryaddress(String deliveryaddress) {
        Deliveryaddress = deliveryaddress;
    }

    public String getPickupaddress() {
        return Pickupaddress;
    }

    public void setPickupaddress(String pickupaddress) {
        Pickupaddress = pickupaddress;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }
}
