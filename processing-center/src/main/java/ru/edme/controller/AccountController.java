package ru.edme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edme.model.Account;
import ru.edme.service.AccountAllService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cards/accounts")
public class AccountController implements IAccountController {

    private final AccountAllService accountAllService;

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountAllService.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountAllService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAll() {
        return ResponseEntity.ok(accountAllService.findAll());
    }

    @PutMapping
    public ResponseEntity<Account> update(@RequestBody Account entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(accountAllService.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean delete = accountAllService.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
