package com.morgan.andy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.morgan.andy.domain.FiniteAutomaton;
import com.morgan.andy.moc.simulation.*;
import com.morgan.andy.service.ModelService;
import com.morgan.andy.web.rest.vm.BulkTestVM;
import com.morgan.andy.web.rest.vm.SimulateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing a created model
 */
@RestController
@RequestMapping("/api")
public class SimulateResource {

    private final Logger log = LoggerFactory.getLogger(SimulateResource.class);

    @Inject
    private DFASimulator dfaSimulator;

    @Inject
    private NFASimulator nfaSimulator;

    @Inject
    private ModelService modelService;

    @Inject
    private SimulatedAutomatonStore simulatedAutomatonStore;

    /**
     * POST  /register : register the user.
     *
     * @param simulateVM the managed user View Model
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the login or e-mail is already in use
     */
    @RequestMapping(value = "/simulate/dfa",
                    method = RequestMethod.POST,
                    produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<?> simulateDfa(@Valid @RequestBody SimulateVM simulateVM, HttpServletRequest request) {
        FiniteAutomaton fa = modelService.convertVmToAutomaton(simulateVM.getFiniteAutomaton());
        Simulation sim = dfaSimulator.loadAutomaton(fa, simulateVM.getInput());
        dfaSimulator.simulateAutomaton(fa, simulateVM.getInput(), sim.getId());
        Map<String, Integer> returnVal = new HashMap<>();
        returnVal.put("id", sim.getId());
        return new ResponseEntity<>(returnVal, HttpStatus.OK);
    }

    @RequestMapping(value = "/simulate/nfa",
                    method = RequestMethod.POST,
                    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> simulateNfa(@Valid @RequestBody SimulateVM simulateVM, HttpServletRequest request) {
        FiniteAutomaton fa = modelService.convertVmToAutomaton(simulateVM.getFiniteAutomaton());
        Simulation sim = dfaSimulator.loadAutomaton(fa, simulateVM.getInput());
        nfaSimulator.simulateAutomaton(fa, simulateVM.getInput(), sim.getId());
        Map<String, Integer> returnVal = new HashMap<>();
        returnVal.put("id", sim.getId());
        return new ResponseEntity<>(returnVal, HttpStatus.OK);
    }

    @RequestMapping(value = "/simulate/{simulationId}/step/{stepId}")
    public ResponseEntity<?> getSimulationStep(@PathVariable("simulationId") int simulationId, @PathVariable("stepId") Integer step) {
        Simulation simulation = simulatedAutomatonStore.getSimulation(simulationId);
        simulation = modelService.removeTransitionsFromSimulation(simulation);
        // return 404 if requesting a step that does not exist
        if(step - 1 > simulation.getSteps().size() - 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(simulation.getSteps().get(step - 1), HttpStatus.OK);
    }

    @RequestMapping(value = "/simulate/{simulationId}/step")
    public ResponseEntity<?> getAllSimulationSteps(@PathVariable("simulationId") int simulationId) {
        Simulation simulation = simulatedAutomatonStore.getSimulation(simulationId);
        simulation = modelService.removeTransitionsFromSimulation(simulation);
        return new ResponseEntity<>(simulation.getSteps(), HttpStatus.OK);
    }

    @RequestMapping(value = "/simulate/{simulationId}/status",
                    method = RequestMethod.GET)
    public ResponseEntity<?> getSimulationStatus(@PathVariable("simulationId") int simulationId) {
        Map<String, String> retVal = new HashMap<>();
        retVal.put("finalState", simulatedAutomatonStore.getSimulation(simulationId).getFinalState().name());
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value = "/test/nfa",
                    method = RequestMethod.POST)
    public ResponseEntity<?> bulkTestNfa(@RequestBody BulkTestVM bulkTestVM) {
        ArrayList<Integer> simulationIds = new ArrayList<>();
        FiniteAutomaton fa = modelService.convertVmToAutomaton(bulkTestVM.getFiniteAutomaton());
        bulkTestVM.getInputs().forEach(i -> {
            Simulation sim = nfaSimulator.loadAutomaton(fa, i.split(""));
            nfaSimulator.simulateAutomaton(fa, i.split(""), sim.getId());
            simulationIds.add(sim.getId());
        });
        return new ResponseEntity<>(simulationIds, HttpStatus.OK);
    }

    @RequestMapping(value = "/test/dfa",
                    method = RequestMethod.POST)
    public ResponseEntity<?> bulkTestDfa(@RequestBody BulkTestVM bulkTestVM) {
        ArrayList<Integer> simulationIds = new ArrayList<>();
        FiniteAutomaton fa = modelService.convertVmToAutomaton(bulkTestVM.getFiniteAutomaton());
        bulkTestVM.getInputs().forEach(i -> {
            Simulation sim = dfaSimulator.loadAutomaton(fa, i.split(""));
            dfaSimulator.simulateAutomaton(fa, i.split(""), sim.getId());
            simulationIds.add(sim.getId());
        });
        return new ResponseEntity<>(simulationIds, HttpStatus.OK);
    }

}
