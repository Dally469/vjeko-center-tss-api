package com.dev.vjeko_api.services;


import com.dev.vjeko_api.entities.HeroData;
import com.dev.vjeko_api.repositories.HeroDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeroService {

    @Autowired
    private HeroDataRepository heroRepository;

    public List<HeroData> getAllHeroes() {
        return heroRepository.findAll();
    }

    public Optional<HeroData> getHeroById(Long id) {
        return heroRepository.findById(id);
    }

    public boolean heroExistsByTitle(String title) {
        return heroRepository.findByTitle(title).isPresent();
    }

    public HeroData saveHero(HeroData hero) {
        return heroRepository.save(hero);
    }

    public void deleteHero(Long id) {
        heroRepository.deleteById(id);
    }
}