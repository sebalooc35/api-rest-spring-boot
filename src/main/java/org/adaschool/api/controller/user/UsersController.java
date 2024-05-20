package org.adaschool.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.adaschool.api.exception.UserNotFoundException;
import org.adaschool.api.repository.user.User;
import org.adaschool.api.repository.user.UserDto;
import org.adaschool.api.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
@Api(value = "controlador de los endpoints para modificar los usuarios", description = "contiene el CRUD para la tabla usuarios")
public class UsersController {

    private final UsersService usersService;

    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    @ApiOperation(value = "crea un nuevo usuario", response = User.class)
    public ResponseEntity<User> createUser(
            @ApiParam(value = "informacion del cliente a registrar", required = true) @RequestBody UserDto userDto) {
        User user = this.usersService.save(new User(userDto));
        return ResponseEntity.created(null).body(user);
    }

    @GetMapping
    @ApiOperation(value = "devuelve todos los clientes registrados en la base de datos", response = List.class)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.usersService.all();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "devuelve el cliente (si existe) con el id indicado", response = List.class)
    public ResponseEntity<User> findById(
            @ApiParam(value = "id del cliente a buscar", required = true) @PathVariable("id") String id) {
        Optional<User> optionalUser = this.usersService.findById(id);
        if (optionalUser.isEmpty()) throw new UserNotFoundException(id);
        return ResponseEntity.ok(optionalUser.get());
    }

    @PutMapping("{id}")
    @ApiOperation(value = "edita (si existe) el usuario con el id indicado con la informacion adjunta", response = User.class)
    public ResponseEntity<User> updateUser(
            @ApiParam(value = "id del usuario a editar", required = true) @PathVariable String id,
            @ApiParam(value = "informacion a editar del usuario", required = true) @RequestBody UserDto userDto) {
        Optional<User> optionalUser = this.usersService.findById(id);
        if (optionalUser.isEmpty()) throw new UserNotFoundException(id);
        User user = optionalUser.get();
        user.update(userDto);
        this.usersService.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "borra (si existe) el usuario con el id indicado", response = void.class)
    public ResponseEntity<Void> deleteUser(
            @ApiParam(value = "id del cliente a borrar", required = true)
            @PathVariable String id) {
        Optional<User> optionalUser = this.usersService.findById(id);
        if (optionalUser.isEmpty()) throw new UserNotFoundException(id);
        this.usersService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
