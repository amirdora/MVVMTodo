package com.codinginflow.mvvmtodo.data

import androidx.room.*
import com.codinginflow.mvvmtodo.ui.tasks.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getAllTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getAllTasksSortedByName(query, hideCompleted)
        }

    @Query("Select * FROM TASK_TABLE WHERE(completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getAllTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("Select * FROM TASK_TABLE WHERE(completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getAllTasksSortedByDateCreated(
        searchQuery: String,
        hideCompleted: Boolean
    ): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}