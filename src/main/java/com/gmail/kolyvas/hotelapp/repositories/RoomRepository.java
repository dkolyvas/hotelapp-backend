package com.gmail.kolyvas.hotelapp.repositories;

import com.gmail.kolyvas.hotelapp.model.Room;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
  //  public List<Room> findAll(Specification<Room> specification);


    public Room findRoomById(Long id);

    public Room findRoomByCode(String code);

    @Query("select  r from Room  r  where r.roomCategory.personsNumber >=?1 and r not in" +
            "( select rd from Room rd join rd.bookings b where b.dateFrom < ?2 and b.dateTo >?3)")
            public List<Room> findAvailableRooms(int personsNo, Date endDate, Date startDate);
}
