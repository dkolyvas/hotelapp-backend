package com.gmail.kolyvas.hotelapp.repositories.specifications;

import jakarta.persistence.criteria.*;
import com.gmail.kolyvas.hotelapp.model.Booking;
import com.gmail.kolyvas.hotelapp.model.Room;

import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class RoomSpeicifications {

    public static Specification<Room> isForPersons(int noPersons){
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThanOrEqualTo(root.get("roomCategory").get("personsNumber"), noPersons);
    }

    public static Specification<Room> isAvailable(Date startDate, Date endDate){
        return (root, query, criteriaBuilder) -> {
            Join<Booking, Room> bookingJoin = root.join("bookings");
            Subquery<Booking> bookingQuery = query.subquery(Booking.class);
            Root<Booking> bookingRoot = bookingQuery.from(Booking.class);
            bookingQuery.select(bookingRoot);
            Predicate idPredicate = criteriaBuilder.equal(bookingRoot.get("room").get("id"), root.get("id"));
            Predicate startDayPredicate = criteriaBuilder.greaterThan(bookingRoot.<Date>get("dateTo"), startDate);
            Predicate endDayPredicate = criteriaBuilder.lessThan(bookingRoot.<Date>get("dateFrom"), endDate);
            bookingQuery.select(bookingRoot).where(idPredicate, startDayPredicate, endDayPredicate);
            return criteriaBuilder.exists(bookingQuery).not();

//            return criteriaBuilder.and(criteriaBuilder.greaterThan(bookingJoin.<Date>get("dateTo"), startDate),
//                    criteriaBuilder.lessThan(bookingJoin.<Date>get("dateFrom"), endDate)).not();
//        });


     };
    }

    public static Specification<Room> isCategory(Long categoryId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("roomCategory").get("id"),categoryId);
    }
}
