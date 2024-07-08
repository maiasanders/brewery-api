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

//    @GetMapping(path = "/breweries/{id}/events")
//    public EventGetResponseDto listByBrewery(@PathVariable int id, @RequestParam(required = false) String date, @RequestParam(required = false) Boolean over21) {
//        return service.listByBrewery(jmmmmn                       )
//    }
}
