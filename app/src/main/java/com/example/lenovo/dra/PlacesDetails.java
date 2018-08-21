package com.example.lenovo.dra;

public class PlacesDetails {

    String nameOfPlace;
    String addressOfPlace;
    String photoLink;
    String place_id;
    String phoneNo;
    Double latitude;
    Double longitude;
    Double rating;

    public PlacesDetails(String nameOfPlace, String addressOfPlace, String photoLink, String place_id, String phoneNo, Double latitude, Double rating, Double longitude) {
        this.nameOfPlace = nameOfPlace;
        this.addressOfPlace = addressOfPlace;
        this.photoLink = photoLink;
        this.place_id = place_id;
        this.phoneNo = phoneNo;
        this.latitude = latitude;
        this.rating = rating;
        this.longitude = longitude;
    }

    public String getNameOfPlace() {
        return nameOfPlace;
    }

    public String getAddressOfPlace() {
        return addressOfPlace;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getRating() {
        return rating;
    }

    public Double getLongitude() {
        return longitude;
    }
}
