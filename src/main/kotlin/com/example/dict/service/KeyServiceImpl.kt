package com.example.dict.service

import com.example.dict.model.Key
import com.example.dict.repository.KeyRepository
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.Optional

@Service
class KeyServiceImpl(private val repository: KeyRepository) : KeyService {

    override fun save(key: Key): Key {
        Assert.hasLength(key.name, "[name] can not be empty")
        return repository.save(key)
    }

    override fun findAll(): List<Key> {
        return repository.findAll()
    }

    override fun findById(id: Long): Optional<Key> {
        return repository.findById(id)
    }

    override fun update(id: Long, key: Key): Optional<Key> {
        val keyOptional = findById(id)
//        if (keyOptional.isEmpty)
//            return Optional.empty<Key>()

        return keyOptional.map {
            val keyToUpdate = it.copy(
                name = key.name,
                document = key.document,
                type = key.type,
                value = key.value
            )
            repository.save(keyToUpdate)
        }
    }

    override fun delete(id: Long) {
        repository.findById(id).map {
            repository.delete(it)
        }
    }

}