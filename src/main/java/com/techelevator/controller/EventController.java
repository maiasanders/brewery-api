package com.techelevator.controller;

import com.techelevator.exception.UnauthorizedUserException;
import com.techelevator.model.dto.EventGetResponseDto;
import com.techelevator.model.dto.EventPostRequestDto;
import com.techelevator.model.dto.EventPostResponseDto;
import com.techelevator.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class EventController {
    private EventService service;

    public EventController(EventService service) {
        try {
            this.service = service;
        } catch (UnauthorizedUserException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(path = "/events")
    @PreAuthorize("hasRole('ROLE_BREWER')")
    public EventPostResponseDto add(@Valid @RequestBody EventPostRequestDto dto, Principal principal) {
        return service.add(dto, principal.getName());
    };

    @GetMapping(path = "events/{id}")
    public EventGetResponseDto get(@PathVariable int id) {
        return service.getEvent(id);
    }

    @GetMapping(path = "/breweries/{id}/events")
    public List<EventGetResponseDto> listByBrewery(@PathVariable int id,
                                                   @RequestParam(required = false) String minDate,
                                                   @RequestParam(required = false) String maxDate,
                                                   @RequestParam(required = false) Boolean over21,
                                                   @RequestParam(required = false) String query) {
        return service.listEventsByBrewery(id, minDate, maxDate, over21, query);
    }
}
