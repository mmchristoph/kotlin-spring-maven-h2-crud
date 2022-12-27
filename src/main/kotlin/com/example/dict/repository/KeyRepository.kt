package com.example.dict.repository

import com.example.dict.model.Key
import org.springframework.data.jpa.repository.JpaRepository

interface KeyRepository : JpaRepository<Key, Long> {
}