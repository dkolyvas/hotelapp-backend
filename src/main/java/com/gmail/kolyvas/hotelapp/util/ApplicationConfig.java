package com.gmail.kolyvas.hotelapp.util;

import com.gmail.kolyvas.hotelapp.dto.booking.BookingShowDTO;
import com.gmail.kolyvas.hotelapp.dto.customer.CustomerInformationShowDTO;
import com.gmail.kolyvas.hotelapp.dto.customer.CustomerShowDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyInsertDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyShowDTO;
import com.gmail.kolyvas.hotelapp.dto.pricingpolicy.PricingPolicyUpdateDTO;
import com.gmail.kolyvas.hotelapp.dto.room.RoomShowDTO;
import com.gmail.kolyvas.hotelapp.dto.user.UserShowDTO;
import com.gmail.kolyvas.hotelapp.model.*;

import com.gmail.kolyvas.hotelapp.util.security.CustomWebSecurity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Import(CustomWebSecurity.class)
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(Customer.class, CustomerShowDTO.class).addMappings(mapper->{
            mapper.map(src-> src.getUser().getUsername(), CustomerShowDTO::setUsername);
            mapper.map(src -> src.getType().getName(), CustomerShowDTO::setCustomerTypeName);
        });
        modelMapper.createTypeMap(CustomerInformation.class, CustomerInformationShowDTO.class).addMappings(mapper ->{
            mapper.map(src -> src.getCustomer().getGivenName(), CustomerInformationShowDTO::setGivenName);
            mapper.map(src->src.getCustomer().getSurname(), CustomerInformationShowDTO::setSurname);
            mapper.map(src ->src.getCustomer().getId(), CustomerInformationShowDTO::setId);
            mapper.map(src-> src.getCustomer().getEmail(), CustomerInformationShowDTO::setEmail);
            mapper.map(src->src.getCustomer().getType().getName(), CustomerInformationShowDTO::setCustomerType);
            mapper.map(src ->src.getCustomer().getPhone(), CustomerInformationShowDTO::setPhoneNo);
            mapper.map(src ->src.getCustomer().getCountry(), CustomerInformationShowDTO::setCountry);

        });

        modelMapper.createTypeMap(Room.class, RoomShowDTO.class).addMappings(mapper->{
            mapper.map(src ->src.getRoomCategory().getDescription(), RoomShowDTO::setCategory);

        });

        modelMapper.createTypeMap(Booking.class, BookingShowDTO.class).addMappings(mapper ->{
            mapper.map(src -> src.getCustomer().getId(), BookingShowDTO::setCustomerId);
            mapper.map(src-> src.getCustomer().getGivenName(), BookingShowDTO::setCustomerName);
            mapper.map(src->src.getCustomer().getSurname(), BookingShowDTO::setCustomerSurname);
            mapper.map(src -> src.getRoom().getId(), BookingShowDTO::setRoomId);
            mapper.map(src->src.getDateFrom(), BookingShowDTO::setDateFrom);
            mapper.map(src-> src.getDateTo(), BookingShowDTO::setDateTo);
            mapper.map(src-> src.getRoom().getCode(), BookingShowDTO::setRoomCode);
        });

        modelMapper.createTypeMap(User.class, UserShowDTO.class).addMappings(mapper ->{
            mapper.map(src -> src.getCustomer().getId(), UserShowDTO::setCustomerId);
        });

        modelMapper.createTypeMap(PricingPolicy.class, PricingPolicyShowDTO.class).addMappings(mapper->{
                mapper.map(src -> src.getRoomCategory().getId(), PricingPolicyShowDTO::setRoomCategoryId);
                mapper.map(src-> src.getCustomerType().getId(), PricingPolicyShowDTO::setCustomerTypeId);
        });

//        modelMapper.createTypeMap(PricingPolicyInsertDTO.class, PricingPolicy.class).addMappings(mapper->{
//            mapper.skip(PricingPolicy::setId);
//            mapper.skip(PricingPolicyInsertDTO::getCustomerTypeId,PricingPolicy::setRoomCategory);
//            mapper.skip(PricingPolicyInsertDTO::getRoomCategoryId, PricingPolicy::setRoomCategory);
//        });

//        modelMapper.createTypeMap(PricingPolicyUpdateDTO.class, PricingPolicy.class).addMappings(mapper->{
//            mapper.skip(PricingPolicyUpdateDTO::g,PricingPolicy::setRoomCategory);
//            mapper.skip(PricingPolicyUpdateDTO::getRoomCategoryId, PricingPolicy::setRoomCategory);
//        });





        return modelMapper;
    }
    @Bean
    public BCryptPasswordEncoder bCrypt(){
        return new BCryptPasswordEncoder();
    }

}

