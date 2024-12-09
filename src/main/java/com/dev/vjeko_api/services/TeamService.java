package com.dev.vjeko_api.services;

import com.dev.vjeko_api.entities.Team;
import com.dev.vjeko_api.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Team member not found with id: " + id));
    }

    @Transactional
    public Team saveTeam(Team team) {
        checkIfTeamMemberExists(team);
        return teamRepository.save(team);
    }

    @Transactional
    public Team updateTeam(Long id, Team teamDetails) {
        Team team = getTeamById(id);
        checkIfTeamMemberExists(teamDetails);

        // Updating fields conditionally
        if (teamDetails.getName() != null && !teamDetails.getName().isEmpty()) {
            team.setName(teamDetails.getName());
        }
        if (teamDetails.getRole() != null && !teamDetails.getRole().isEmpty()) {
            team.setRole(teamDetails.getRole());
        }
        if (teamDetails.getImg() != null && !teamDetails.getImg().isEmpty()) {
            team.setImg(teamDetails.getImg());
        }
        if (teamDetails.getDescription() != null && !teamDetails.getDescription().isEmpty()) {
            team.setDescription(teamDetails.getDescription());
        }
        if (teamDetails.getEmail() != null && !teamDetails.getEmail().isEmpty()) {
            team.setEmail(teamDetails.getEmail());
        }

        if (teamDetails.getPhone() != null && !teamDetails.getPhone().isEmpty()) {
            team.setPhone(teamDetails.getPhone());
        }


        return teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(Long id) {
        Team team = getTeamById(id);
        teamRepository.delete(team);
    }

    private void checkIfTeamMemberExists(Team team) {
        Optional<Team> existingTeam = teamRepository.findByNameAndRole(team.getName(), team.getRole());
        if (existingTeam.isPresent() && !existingTeam.get().getId().equals(team.getId())) {
            throw new IllegalArgumentException("A team member with the same name and role already exists.");
        }
    }
    public boolean teamMemberExistsByEmail(String email) {
        Optional<Team> team = teamRepository.findByEmail(email);
        return team.isPresent();
    }

    public boolean teamMemberExistsByPhone(String phone) {
        Optional<Team> team = teamRepository.findByPhone(phone);
        return team.isPresent();
    }

}