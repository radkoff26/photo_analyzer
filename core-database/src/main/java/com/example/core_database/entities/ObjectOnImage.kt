package com.example.core_database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Объект определённой картинки.
 * @param id id объекта (генерируется автоматически - суррогатный PK)
 * @param imageId id картинки, которой принадлежит данный объект
 * @param type тип объекта картинки
 * @param left левая координата объекта на картинке
 * @param top верхняя координата объекта на картинке
 * @param right правая координата объекта на картинке
 * @param bottom нижняя координата объекта на картинке
 * */
@Entity(tableName = "object")
data class ObjectOnImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
    val imageId: Long,
    val type: ObjectOnImageType,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)