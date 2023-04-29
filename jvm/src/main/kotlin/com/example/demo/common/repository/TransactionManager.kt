package com.example.demo.common.repository

interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}
