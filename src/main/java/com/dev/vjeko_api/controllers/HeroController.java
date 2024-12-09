package com.dev.vjeko_api.controllers;

import com.dev.vjeko_api.custom.ApiResponse;
import com.dev.vjeko_api.entities.HeroData;
import com.dev.vjeko_api.services.AuthService;
import com.dev.vjeko_api.services.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/heroes")
@RequiredArgsConstructor
public class HeroController {

    @Autowired
    private  HeroService heroService;

    @Autowired
    private  AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllHeroes() {
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Fetched all heroes", heroService.getAllHeroes()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getHeroById(@PathVariable Long id) {
        Optional<HeroData> hero = heroService.getHeroById(id);
        if (hero.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Hero fetched successfully", hero.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Hero not found"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createHero(@RequestBody HeroData hero, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            if (heroService.heroExistsByTitle(hero.getTitle())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(HttpStatus.CONFLICT.value(), false, "Hero with this title already exists"));
            }
            HeroData savedHero = heroService.saveHero(hero);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(HttpStatus.CREATED.value(), true, "Hero created successfully", savedHero));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateHero(@PathVariable Long id, @RequestBody HeroData hero, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            Optional<HeroData> existingHero = heroService.getHeroById(id);
            if (existingHero.isPresent()) {
                existingHero.get().setImg(hero.getImg());
                existingHero.get().setTitle(hero.getTitle());
                existingHero.get().setSubtitle(hero.getSubtitle());
                existingHero.get().setDescription(hero.getDescription());
                HeroData updatedHero = heroService.saveHero(existingHero.get());
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Hero updated successfully", updatedHero));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Hero not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteHero(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            if (heroService.getHeroById(id).isPresent()) {
                heroService.deleteHero(id);
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Hero deleted successfully"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Hero not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }
}