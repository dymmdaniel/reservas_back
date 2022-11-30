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
import java.util.*;

@RestController
@RequestMapping("/api/user")
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
        User userExist = userService.findByEmail(user.getEmail());
        if(userExist != null){
            log.info("El usuario ya existe.");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya existe");
        }
        User userEntity = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id,@Valid @RequestBody User user, BindingResult result){
        if(result.hasErrors()){
            return this.validar(result);
        }
        User userExist = userService.find(id);
        if(userExist == null){
            return ResponseEntity.notFound().build();
        }
        log.info(user.getRol().getDescription());
        User usr = userExist;
        usr.setCompany(user.getCompany());
        usr.setRol(user.getRol());
        usr.setEmail(user.getEmail());
        usr.setPassword(user.getPassword());
        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setPhone(user.getPhone());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(usr));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
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
