package com.example.TravelApp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private double price;
    private double rating;
    private String imageUrl;
    private String category;
    private int maxTravelers;

    @Column(columnDefinition = "TEXT")
    private String mapUrl;

    // 🎯 "What's Included" කරුණු ටික සේව් කරගන්න මේ ලයින් එක එකතු කළා
    @Column(columnDefinition = "TEXT")
    private String includes;

    @ManyToOne
    private Destination destination;

    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // 🎯 හෝටල් ලැයිස්තුව පැකේජ් එකට සම්බන්ධ කිරීම (@OneToMany)
    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Hotel> hotels = new ArrayList<>();

    // 🎯 1. Default Constructor
    public TourPackage() {
    }

    // 🎯 2. පරණ Constructor එක (DataLoader සහ AdminController එකේ Build Error එක නැති කිරීමට මෙය එකතු කළා)
    public TourPackage(String name, String description, double price, double rating, String imageUrl, String category, int maxTravelers, Destination destination) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.category = category;
        this.maxTravelers = maxTravelers;
        this.destination = destination;
    }

    // 🎯 3. අලුත් Constructor එක (mapUrl එකත් සහිතව)
    public TourPackage(String name, String description, double price, double rating, String imageUrl, String category, int maxTravelers, String mapUrl, Destination destination) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.category = category;
        this.maxTravelers = maxTravelers;
        this.mapUrl = mapUrl;
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMaxTravelers() {
        return maxTravelers;
    }

    public void setMaxTravelers(int maxTravelers) {
        this.maxTravelers = maxTravelers;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    // 🎯 Includes සඳහා අදාළ Getter සහ Setter මෙතනට එකතු කළා
    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // 🎯 Hotels සඳහා අදාළ Getter සහ Setter එකතු කළා
    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }
}