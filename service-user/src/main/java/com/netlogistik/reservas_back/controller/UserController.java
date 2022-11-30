package com.netlogistik.reservas_back.controller;

import com.netlogistik.reservas_back.model.Rol;
import com.netlogistik.reservas_back.model.User;
import com.netlogistik.reservas_back.service.RolService;
import com.netlogistik.reservas_back.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RolService rolService;

    @GetMapping("/")
    public ResponseEntity<List<User>> listUser(){
        List<User> optional = userService.listUsers();
        if(optional==null){
            return ResponseEntity.notFound().build();
        }
        log.info(optional.toString());
        return ResponseEntity.ok(optional);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        log.info(user.getEmail() + " "+user.getPassword());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result){
        if(result.hasErrors()){
            return this.validar(result);
        }
        if(user.getRol()==null){
            log.info("Se crea el rol por defecto nivel USER");
            Rol rol = rolService.find((long) 1); //Rol por defecto
            user.setRol(rol);
        }
        User userEntity = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validar(BindingResult result){
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "Error en el atributo: " + err.getField() + ", " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
