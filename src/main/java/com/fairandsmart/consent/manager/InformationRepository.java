package com.fairandsmart.consent.manager;

import com.fairandsmart.consent.manager.entity.Information;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InformationRepository implements PanacheRepository<Information> {

    

}
