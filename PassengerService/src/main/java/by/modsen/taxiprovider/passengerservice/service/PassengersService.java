package by.modsen.taxiprovider.passengerservice.service;

import by.modsen.taxiprovider.passengerservice.client.RatingHttpClient;
import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerListDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengersPageDto;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDto;
import by.modsen.taxiprovider.passengerservice.mapper.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
import by.modsen.taxiprovider.passengerservice.util.validation.PassengersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static by.modsen.taxiprovider.passengerservice.util.Status.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengersService {

    private final PassengersRepository passengersRepository;

    private final PassengerMapper passengerMapper;

    private final RatingHttpClient ratingHttpClient;

    private final PassengersValidator passengersValidator;

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    private static final String KAFKA_TOPIC_NAME = "RIDE";

    public PassengerListDto findAll() {
        List<Passenger> passengers = passengersRepository.findByStatusOrderByLastname(PASSENGER_ACCOUNT_STATUS_ACTIVE);

        return PassengerListDto.builder()
                .content(passengerMapper.toListDto(passengers))
                .build();
    }

    public PassengersPageDto findPagePassengers(int index, int count, String sortField)
            throws InvalidRequestDataException {
        if ((index <= 0) || (count <= 0)) {
            throw new InvalidRequestDataException(INVALID_PAGE_REQUEST);
        }

        List<Passenger> passengers = passengersRepository
                .findAll(PageRequest.of(index - 1, count, Sort.by(sortField))).getContent()
                .stream()
                .filter(passenger -> passenger.getStatus().equals(PASSENGER_ACCOUNT_STATUS_ACTIVE))
                .toList();

        return PassengersPageDto.builder()
                .content(passengerMapper.toListDto(passengers))
                .page(index)
                .size(count)
                .build();
    }

    public PassengerDto findById(long id) throws EntityNotFoundException {
        return passengerMapper.toDto(passengersRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, id))));
    }

    private Passenger findPassenger(long id) throws EntityNotFoundException {
        return passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, id)));
    }

    @Transactional
    public PassengerResponseDto save(NewPassengerDto passengerDTO)
            throws EntityNotFoundException, EntityValidateException {
        Passenger passenger = passengerMapper.toEntity(passengerDTO);

        passengersValidator.validate(passenger);

        passenger.setRole(PASSENGER_ROLE_NAME);
        passenger.setStatus(PASSENGER_ACCOUNT_STATUS_ACTIVE);

        passengersRepository.save(passenger);

        Passenger createdPassenger = passengersRepository
                .findByEmail(passenger.getEmail())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PASSENGER_NOT_CREATED, passenger.getEmail())));

        ratingHttpClient.initPassengerRating(createdPassenger.getId());

        return new PassengerResponseDto(createdPassenger.getId());
    }

    @Transactional
    public PassengerResponseDto update(long id, PassengerDto passengerDTO)
            throws EntityNotFoundException, EntityValidateException {
        Passenger passengerData = findPassenger(id);

        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passenger.setId(id);

        String firstname = passenger.getFirstname();
        String lastname = passenger.getLastname();
        String email = passenger.getEmail();
        String phoneNumber = passenger.getPhoneNumber();

        if (email != null) {
            passengerData.setEmail(email);
        }
        if (firstname != null) {
            passengerData.setFirstname(firstname);
        }
        if (lastname != null) {
            passengerData.setLastname(lastname);
        }
        if (phoneNumber != null) {
            passengerData.setPhoneNumber(phoneNumber);
        }

        passengersValidator.validate(passenger);

        passengersRepository.save(passengerData);

        return new PassengerResponseDto(id);
    }

    @Transactional
    public PassengerResponseDto deactivate(long id) throws EntityNotFoundException {
        Passenger passenger = findPassenger(id);

        passenger.setStatus(PASSENGER_ACCOUNT_STATUS_INACTIVE);

        passengersRepository.save(passenger);

        return new PassengerResponseDto(id);
    }

    public PassengerProfileDto getPassengerProfile(long id) throws EntityNotFoundException {
        PassengerDto passenger = findById(id);

        return PassengerProfileDto.builder()
                .passenger(passenger)
                .rating(ratingHttpClient.getPassengerRating(id).getValue())
                .build();
    }

    @KafkaListener(topics = KAFKA_TOPIC_NAME)
    private void messageListener(String message) {
        //TODO: change to log
        System.out.println(message);
    }
}
