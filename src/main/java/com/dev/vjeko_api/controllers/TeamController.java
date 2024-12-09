package com.dev.vjeko_api.controllers;

import com.dev.vjeko_api.custom.ApiResponse;
import com.dev.vjeko_api.entities.Team;
import com.dev.vjeko_api.services.AuthService;
import com.dev.vjeko_api.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Fetched all teams", teams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTeamById(@PathVariable Long id) {
        try {
            Team team = teamService.getTeamById(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Team fetched successfully", team));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTeam(@RequestBody Team team, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            if (teamService.teamMemberExistsByEmail(team.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ApiResponse(HttpStatus.CONFLICT.value(), false, "Team member with this email already exists")
                );
            }

            if (teamService.teamMemberExistsByPhone(team.getPhone())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ApiResponse(HttpStatus.CONFLICT.value(), false, "Team member with this phone number already exists")
                );
            }

            Team savedTeam = teamService.saveTeam(team);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(HttpStatus.CREATED.value(), true, "Team created successfully", savedTeam));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTeam(@PathVariable Long id, @RequestBody Team team, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            Team updatedTeam = teamService.updateTeam(id, team);
            if (updatedTeam != null) {
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Team updated successfully", updatedTeam));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Team not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTeam(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            teamService.deleteTeam(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Team deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }
}