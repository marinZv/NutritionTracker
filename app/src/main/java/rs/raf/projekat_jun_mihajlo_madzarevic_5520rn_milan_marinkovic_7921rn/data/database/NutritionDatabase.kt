package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.CalorieIngredientDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.CalorieMealDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.CategoryDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.MealDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.SavedMealDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.datasources.local.UserDao
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieIngredientEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CalorieMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.CategoryEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.MealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.SavedMealEntity
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.data.models.UserEntity

@Database(
    entities = [UserEntity::class,
               CategoryEntity::class,
               MealEntity::class,
               SavedMealEntity::class,
               CalorieMealEntity::class,
               CalorieIngredientEntity::class],
    version = 7,
    exportSchema = false)
abstract class NutritionDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getCategoryDao(): CategoryDao

    abstract fun getMealDao(): MealDao

    abstract fun getSavedMealDao(): SavedMealDao

    abstract fun getCalorieMealDao(): CalorieMealDao

    abstract fun getCalorieIngredientDao(): CalorieIngredientDao
}