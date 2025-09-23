package com.sistema.barbline.application;

import com.sistema.barbline.entities.Barbeiro;
import com.sistema.barbline.repositories.BarbeiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BarbeiroApplication {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    public List listar(){
        return barbeiroRepository.findAll();
    }

    public Barbeiro cadastrar(Barbeiro barbeiros){
        return barbeiroRepository.save(barbeiros);
    }

    public void excluir(String id){
        barbeiroRepository.deleteById(id);
    }

    public void atualizar(Barbeiro barbeiros){
        barbeiroRepository.save(barbeiros);
    }

}
