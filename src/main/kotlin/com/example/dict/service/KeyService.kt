package com.example.dict.service

import com.example.dict.model.Key
import java.util.Optional

interface KeyService {

    fun save(key: Key): Key

    fun findAll(): List<Key>

    fun findById(id: Long): Optional<Key>

    fun update(id: Long, key: Key): Optional<Key>

    fun delete(id: Long)

}