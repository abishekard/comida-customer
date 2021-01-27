package com.abishek.comida.address;

public class AddressModel {

    private String addressId;
    private String address;
    private String state;
    private String city;
    private String pinCode;
    private String landmark;
    private String locality;
    private String lat;
    private String lng;
    private String addressType;

    public AddressModel(String addressId, String address, String state,
                        String city, String pinCode, String landmark,
                        String locality, String lat, String lng,String addressType) {
        this.addressId = addressId;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pinCode = pinCode;
        this.landmark = landmark;
        this.locality = locality;
        this.lat = lat;
        this.lng = lng;
        this.addressType = addressType;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getLocality() {
        return locality;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
