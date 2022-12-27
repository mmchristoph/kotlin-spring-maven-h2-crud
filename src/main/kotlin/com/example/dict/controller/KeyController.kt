package com.example.dict.controller

import com.example.dict.model.Key
import com.example.dict.service.KeyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("dict")
class KeyController(private val service: KeyService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody key: Key): Key = service.save(key)

    @GetMapping
    fun findAll(): List<Key> = service.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Key> =
        service.findById(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody key: Key): ResponseEntity<Key> =
        service.update(id, key).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }

}