package com.dev.vjeko_api.repositories;

import com.dev.vjeko_api.entities.HeroData;
import org.springframework.data.jpa.repository.JpaRepository;.


import java.util.Optional;

public interface HeroDataRepository extends JpaRepository<HeroData, Long> {
    Optional<HeroData> findByTitle(String title);

}