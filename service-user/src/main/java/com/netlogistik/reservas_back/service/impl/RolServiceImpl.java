package com.netlogistik.reservas_back.service.impl;

import com.netlogistik.reservas_back.model.Rol;
import com.netlogistik.reservas_back.repository.RolRepository;
import com.netlogistik.reservas_back.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public Rol find(Long id) {
        return rolRepository.findById(id).orElse(null);
    }
}
