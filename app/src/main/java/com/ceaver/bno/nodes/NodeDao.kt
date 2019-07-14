package com.ceaver.bno.nodes

import android.arch.persistence.room.*

@Dao
interface NodeDao {
    @Query("select * from node")
    fun loadNodes(): List<Node>

    @Query("select * from node where id = :id")
    fun loadNode(id: Long): Node

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertNode(node: Node)

    @Update
    fun updateNode(node: Node)

    @Delete
    fun deleteNode(node: Node)
}