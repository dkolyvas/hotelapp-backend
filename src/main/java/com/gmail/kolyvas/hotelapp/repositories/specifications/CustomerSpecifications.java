package com.gmail.kolyvas.hotelapp.repositories.specifications;

import com.gmail.kolyvas.hotelapp.model.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {

    public static Specification<Customer> hasPassport(String passportNo){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("passportNo"),passportNo);
    }

    public static Specification<Customer> isTypeOf(Long typeId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type").get("id"), typeId);
    }

    public static Specification<Customer> hasSurnameLike(String surname){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("surname"), surname + "%");
    }

    public static Specification<Customer> hasGivenNameLike(String givenName){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("givenName"), givenName +"%");
    }

    public static Specification<Customer> hasEmail(String email){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<Customer> hasPhoneNumber(String phoneNumber){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phone"), phoneNumber);
    }

    public static Specification<Customer> isFromCountry(String country){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("country"), country);
    }

 /*   public static Specification<Customer> hasMoreBookingsThanSince(int noOfBookings, Date since){
        return (root, query, criteriaBuilder) -> {
            Join<Customer, Booking> bookingJoin = root.join("bookings");


                    criteriaBuilder.count(bookingJoin.get("id")).
                            in(criteriaBuilder.greaterThanOrEqualTo(bookingJoin.get("dateFrom"),since)).equals(since).;

        }
    }*/



}
