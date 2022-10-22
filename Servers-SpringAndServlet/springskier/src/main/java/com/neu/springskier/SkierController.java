package com.neu.springskier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skiers")
public class SkierController {
    @PostMapping("/{resortId}/seasons/{seasonId}/days/{dayId}/skiers/{skierId}")
    public ResponseEntity<Response> skier(@PathVariable Integer resortId,
                                @PathVariable Integer seasonId,
                                @PathVariable Integer dayId,
                                @PathVariable Integer skierId,
                                @RequestBody LiftRide liftRide
                        ) {
        createLiftRideEvent(resortId, seasonId, dayId, skierId, liftRide);
        return new ResponseEntity<Response>(new Response("Created Successfully"), HttpStatus.CREATED);
    }

    public LiftRideEvent createLiftRideEvent(Integer resortId, Integer seasonId, Integer dayId, Integer skierId, LiftRide liftRide) {

        // create object
        return new LiftRideEvent(liftRide, resortId, seasonId.toString(), dayId.toString(), skierId);
    }
}
